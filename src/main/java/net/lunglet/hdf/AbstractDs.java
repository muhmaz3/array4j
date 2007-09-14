package net.lunglet.hdf;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// TODO check datatypes used in read/write methods

abstract class AbstractDs extends H5Object {
    public AbstractDs(final int id) {
        super(id);
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
        checkBuffer(size, memType, memSpace, fileSpace);
    }

    protected void checkBuffer(final int size, final DataType memType) {
        checkBuffer(size, memType, DataSpace.ALL, DataSpace.ALL);
    }

    protected void checkBuffer(final int size, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace) {
        final DataSpace space;
        if (memSpace.equals(DataSpace.ALL)) {
            if (fileSpace.equals(DataSpace.ALL)) {
                space = getSpace();
            } else {
                space = fileSpace;
            }
        } else {
            space = memSpace;
        }
        // TODO need a better check here that takes offset and bounding box into
        // account, because this check won't catch all buffers that are too
        // small
        long requiredSize = memType.getSize() * space.getSelectNPoints();
        if (size < requiredSize) {
            throw new IllegalArgumentException();
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
