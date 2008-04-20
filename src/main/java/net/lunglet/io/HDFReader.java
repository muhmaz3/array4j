package net.lunglet.io;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Iterator;
import net.jcip.annotations.NotThreadSafe;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.packed.FloatPackedMatrix;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.DataSpace;
import net.lunglet.hdf.FloatType;
import net.lunglet.hdf.H5File;
import net.lunglet.hdf.SelectionOperator;
import net.lunglet.util.BufferUtils;
import net.lunglet.util.NumberUtils;
import org.apache.commons.lang.NotImplementedException;

// TODO handle any order, stride, offset, size, etc.

@NotThreadSafe
public final class HDFReader implements Closeable {
    private static final class MatrixSelector implements Iterable<DataSpace> {
        private final int capacity;

        private final DataSpace space;

        /**
         * @param capacity size of buffer in number of elements
         */
        public MatrixSelector(final DataSpace space, final int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException();
            }
            this.space = space;
            this.capacity = capacity;
        }

        @Override
        public Iterator<DataSpace> iterator() {
            final int[] dims = space.getIntDims();
            if (dims.length != 2) {
                throw new NotImplementedException();
            }
            final int rows = dims[0];
            final int columns = dims[1];
            final long[] count = {1, 1};
            return new Iterator<DataSpace>() {
                private int column = 0;

                private int row = 0;

                @Override
                public boolean hasNext() {
                    return row < rows && column < columns;
                }

                @Override
                public DataSpace next() {
                    space.selectNone();
                    int remaining = capacity;

                    // try to read remainder of current row
                    remaining = readColumns(remaining);

                    if (remaining == 0 || row == rows) {
                        return space;
                    }

                    // try to read complete rows
                    while (remaining >= columns && row < rows) {
                        long[] start = {row, column};
                        long[] block = {1, columns};
                        space.selectHyperslab(SelectionOperator.OR, start, null, count, block);
                        row++;
                        column = 0;
                        remaining -= columns;
                    }

                    if (remaining == 0 || row == rows) {
                        return space;
                    }

                    // try to read part of current row
                    remaining = readColumns(remaining);
                    return space;
                }

                private int readColumns(final int origCapacity) {
                    int capacity = origCapacity;
                    int columnsRemaining = columns - column;
                    int columnsToRead = Math.min(capacity, columnsRemaining);
                    if (columnsToRead > 0) {
                        long[] start = {row, column};
                        long[] block = {1, columnsToRead};
                        space.selectHyperslab(SelectionOperator.OR, start, null, count, block);
                        column += columnsToRead;
                        if (column == columns) {
                            row++;
                            column = 0;
                        }
                        capacity -= columnsToRead;
                    }
                    return capacity;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;

    private final ByteBuffer buffer;

    private final H5File h5file;

    public HDFReader(final H5File h5file) {
        this(h5file, DEFAULT_BUFFER_SIZE);
    }

    /**
     * @param bufSize read buffer size in bytes
     */
    public HDFReader(final H5File h5file, final int bufSize) {
        this.h5file = h5file;
        if (bufSize > 0) {
            this.buffer = BufferUtils.createAlignedBuffer(bufSize, 1);
        } else {
            this.buffer = null;
        }
    }

    /**
     * Create a reader that is not associated with a specific file.
     */
    public HDFReader(final int bufSize) {
        this((H5File) null, bufSize);
    }

    public HDFReader(final String name) {
        this(new H5File(name, H5File.H5F_ACC_RDONLY), DEFAULT_BUFFER_SIZE);
    }

    public HDFReader(final String name, final int bufSize) {
        this(new H5File(name, H5File.H5F_ACC_RDONLY), bufSize);
    }

    public void close() {
        if (h5file != null) {
            h5file.close();
        }
    }

    private ByteBuffer getBuffer() {
        if (buffer == null) {
            throw new IllegalStateException();
        }
        return (ByteBuffer) buffer.position(0);
    }

    public H5File getH5File() {
        if (h5file == null) {
            throw new IllegalStateException();
        }
        return h5file;
    }

    /**
     * Read matrix from file.
     * <p>
     * This method is provided to facilitate reuse of a single HDFReader
     * instance containing a direct buffer while reading many heap matrices.
     */
    public void read(final H5File h5file, final String name, final FloatDenseMatrix matrix) {
        readImpl(h5file, name, matrix);
    }

    public void read(final String name, final FloatDenseMatrix matrix) {
        if (h5file == null) {
            throw new IllegalStateException();
        }
        readImpl(h5file, name, matrix);
    }

    public void read(final String name, final FloatPackedMatrix matrix) {
        read(name, matrix, DEFAULT_BUFFER_SIZE);
    }

    public void read(final String name, final FloatPackedMatrix matrix, final int bufSize) {
        if (bufSize <= 0) {
            throw new IllegalArgumentException();
        }
        DataSet dataset = null;
        DataSpace space = null;
        try {
            dataset = h5file.getRootGroup().openDataSet(name);
            space = dataset.getSpace();
            long[] dims = space.getDims();
            if (dims.length != 1) {
                throw new RuntimeException(new IOException());
            }
            int k = (int) dims[0];
            int n = ((int) Math.sqrt(1 + 8 * k) - 1) / 2;
            if (matrix.rows() != n) {
                throw new IllegalArgumentException();
            }
            if (matrix.storage().equals(Storage.DIRECT)) {
                dataset.read(matrix.data(), FloatType.IEEE_F32LE);
            } else {
                FloatBuffer data = matrix.data();
                // TODO don't allocate this buffer each time
                FloatBuffer buf = BufferUtils.createFloatBuffer(bufSize, Storage.DIRECT);
                DataSpace memSpace = new DataSpace(buf.capacity());
                for (int i = 0; i < data.capacity(); i += buf.capacity()) {
                    int remaining = data.capacity() - i;
                    int count = Math.min(remaining, buf.capacity());
                    buf.limit(count);
                    memSpace.selectHyperslab(SelectionOperator.SET, new long[]{0}, new long[]{count});
                    space.selectHyperslab(SelectionOperator.SET, new long[]{i}, new long[]{count});
                    dataset.read(buf, FloatType.IEEE_F32LE, memSpace, space);
                    buf.rewind();
                    data.put(buf);
                }
                memSpace.close();
            }
        } finally {
            if (space != null) {
                space.close();
            }
            if (dataset != null) {
                dataset.close();
            }
        }
    }

    private void readImpl(final H5File h5file, final String name, final FloatDenseMatrix matrix) {
        if (matrix.rows() > 1 && matrix.columns() > 1 && !matrix.order().equals(Order.ROW)) {
            throw new NotImplementedException();
        }
        DataSet dataset = null;
        DataSpace space = null;
        try {
            dataset = h5file.getRootGroup().openDataSet(name);
            space = dataset.getSpace();
            int[] dims = space.getIntDims();
            if (dims.length != 2 && dims.length != 1) {
                throw new RuntimeException(new IOException());
            }
            if (dims.length == 1) {
                // TODO allow matrices with a single row or column when the
                // dimension is 1
                dims = new int[]{dims[0], 1};
            }
            if (dims[0] > 1 && dims[1] > 1) {
                if (matrix.rows() != dims[0] || matrix.columns() != dims[1]) {
                    throw new IllegalArgumentException();
                }
            } else {
                int minDim = Math.min(dims[0], dims[1]);
                int maxDim = Math.max(dims[0], dims[1]);
                if (!((matrix.rows() == minDim && matrix.columns() == maxDim)
                        || (matrix.rows() == maxDim && matrix.columns() == minDim))) {
                    throw new IllegalArgumentException();
                }
            }
            if (matrix.storage().equals(Storage.DIRECT)) {
                dataset.read(matrix.data(), FloatType.IEEE_F32LE);
            } else {
                FloatBuffer buf = getBuffer().asFloatBuffer();
                if (buf.capacity() < 1) {
                    throw new IllegalStateException("Buffer is too small to contain a float");
                }
                FloatBuffer data = matrix.data();
                space.selectAll();
                int npoints = NumberUtils.castLongToInt(space.getSelectNPoints());
                if (buf.capacity() >= npoints) {
                    buf.limit(npoints);
                    dataset.read(buf, FloatType.IEEE_F32LE);
                    buf.rewind();
                    data.put(buf);
                } else {
                    DataSpace memSpace = new DataSpace(buf.capacity());
                    final long[] start = {0};
                    final long[] count = {1};
                    final long[] block = new long[1];
                    for (DataSpace selectedSpace : new MatrixSelector(space, buf.capacity())) {
                        int selectNPoints = NumberUtils.castLongToInt(selectedSpace.getSelectNPoints());
                        buf.limit(selectNPoints);
                        block[0] = selectNPoints;
                        memSpace.selectHyperslab(SelectionOperator.SET, start, null, count, block);
                        // TODO check if reading with ALL memory space is faster
                        dataset.read(buf, FloatType.IEEE_F32LE, memSpace, selectedSpace);
                        buf.rewind();
                        data.put(buf);
                    }
                    memSpace.close();
                }
            }
        } finally {
            if (space != null) {
                space.close();
            }
            if (dataset != null) {
                dataset.close();
            }
        }
    }
}
