package net.lunglet.hdf;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// TODO check datatypes used in read/write methods

abstract class AbstractDs extends H5Object {
    public AbstractDs(final int id, final CloseAction closeAction) {
        super(id, closeAction);
    }

    protected void checkBuffer(final Buffer buf, final DataType memType) {
        checkBuffer(buf, memType, DataSpace.ALL, DataSpace.ALL);
    }

    protected void checkBuffer(final Buffer buf, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace) {
        final int size;
        if (buf instanceof ByteBuffer) {
            size = buf.capacity();
        } else if (buf instanceof FloatBuffer) {
            size = H5Library.FLOAT_BYTES * buf.capacity();
        } else if (buf instanceof DoubleBuffer) {
            size = H5Library.DOUBLE_BYTES * buf.capacity();
        } else if (buf instanceof IntBuffer) {
            size = H5Library.INTEGER_BYTES * buf.capacity();
        } else {
            // TODO add other buffers above
            throw new AssertionError();
        }
        checkSize(size, memType, memSpace, fileSpace);
    }

    protected void checkSize(final int size, final DataType memType) {
        checkSize(size, memType, DataSpace.ALL, DataSpace.ALL);
    }

    protected void checkSize(final int size, final DataType memType, final DataSpace memSpace, final DataSpace fileSpace) {
        final DataSpace space;
        boolean closeSpace = false;
        if (memSpace.equals(DataSpace.ALL)) {
            if (fileSpace.equals(DataSpace.ALL)) {
                space = getSpace();
                closeSpace = true;
            } else {
                space = fileSpace;
            }
        } else {
            space = memSpace;
        }
        // TODO need a more thorough bounds check here
        long requiredSize = memType.getSize() * space.getSelectNPoints();
        try {
            if (size < requiredSize) {
                throw new IllegalArgumentException();
            }
        } finally {
            if (closeSpace) {
                space.close();
            }
        }
    }

    public abstract DataSpace getSpace();

    public abstract long getStorageSize();

    public abstract DataType getType();

    public abstract void read(Buffer buf, DataType memType);

    public final void read(final int[] arr) {
        read(IntBuffer.wrap(arr), IntType.STD_I32LE);
    }

    public abstract void write(Buffer buf, DataType memType);

    public final void write(final float[] arr) {
        write(FloatBuffer.wrap(arr), FloatType.IEEE_F32LE);
    }

    public final void write(final int[] arr) {
        write(IntBuffer.wrap(arr), IntType.STD_I32LE);
    }
}
