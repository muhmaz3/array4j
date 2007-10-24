package com.googlecode.array4j.io;

import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.packed.FloatPackedMatrix;
import com.googlecode.array4j.util.BufferUtils;
import java.io.IOException;
import java.nio.FloatBuffer;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.DataSpace;
import net.lunglet.hdf.FloatType;
import net.lunglet.hdf.H5File;
import net.lunglet.hdf.SelectionOperator;

// TODO handle any orientation, stride, offset, size, etc.

// TODO write generic function to do direct buffered reads from any dataset into a heap buffer

public final class HDFReader {
    private static final int DEFAULT_BUFFER_SIZE = 4 * 1024 * 1024;

    private final H5File h5file;

    public HDFReader(final H5File h5file) {
        this.h5file = h5file;
    }

    public void close() {
        h5file.close();
    }

    public void read(final String name, final FloatDenseMatrix matrix) {
        if (!matrix.order().equals(Order.ROW)) {
            throw new UnsupportedOperationException();
        }
        DataSet dataset = null;
        DataSpace space = null;
        try {
            dataset = h5file.getRootGroup().openDataSet(name);
            space = dataset.getSpace();
            long[] dims = space.getDims();
            if (dims.length != 2 && dims.length != 1) {
                throw new RuntimeException(new IOException());
            }
            if (dims.length == 1) {
                // TODO allow matrices with a single row or column when the
                // dimension is 1
                dims = new long[]{dims[0], 1};
            }
            if (matrix.rows() != dims[0] || matrix.columns() != dims[1]) {
                throw new IllegalArgumentException();
            }
            if (matrix.storage().equals(Storage.DIRECT)) {
                dataset.read(matrix.data(), FloatType.IEEE_F32LE);
            } else {
                throw new UnsupportedOperationException();
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
}
