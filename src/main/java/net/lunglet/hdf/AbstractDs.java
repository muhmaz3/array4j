package net.lunglet.hdf;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// TODO check datatypes used in read/write methods

abstract class AbstractDs extends H5Object {
    private static final int DOUBLE_BYTES = Double.SIZE >>> 3;

    private static final int FLOAT_BYTES = Float.SIZE >>> 3;

    private static final int INTEGER_BYTES = Integer.SIZE >>> 3;

    public AbstractDs(final int id) {
        super(id);
    }

    public abstract DataSpace getSpace();

    public abstract DataType getType();

    public abstract long getStorageSize();

    public abstract void read(Buffer buf, DataType memType);

    public abstract void write(Buffer buf, DataType memType);

    protected void checkBuffer(final Buffer buf, final DataType memType) {
        checkBuffer(buf, memType, DataSpace.ALL, DataSpace.ALL);
    }

    protected void checkBuffer(final int size, final DataType memType) {
        checkBuffer(size, memType, DataSpace.ALL, DataSpace.ALL);
    }

    protected void checkBuffer(final Buffer buf, final DataType memType, final DataSpace memSpace,
            final DataSpace fileSpace) {
        final int size;
        if (buf instanceof ByteBuffer) {
            size = buf.capacity();
        } else if (buf instanceof FloatBuffer) {
            size = FLOAT_BYTES * buf.capacity();
        } else if (buf instanceof DoubleBuffer) {
            size = DOUBLE_BYTES * buf.capacity();
        } else if (buf instanceof IntBuffer) {
            size = INTEGER_BYTES * buf.capacity();
        } else {
            // TODO add other buffers above
            throw new AssertionError();
        }
        checkBuffer(size, memType, memSpace, fileSpace);
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

    public final void write(final int[] arr) {
        write(IntBuffer.wrap(arr), PredefinedType.STD_I32LE);
    }

    public final void write(final float[] arr) {
        write(FloatBuffer.wrap(arr), PredefinedType.IEEE_F32LE);
    }

    public final void read(final int[] arr) {
        read(IntBuffer.wrap(arr), PredefinedType.STD_I32LE);
    }
}
