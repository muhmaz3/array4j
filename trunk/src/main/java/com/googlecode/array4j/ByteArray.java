package com.googlecode.array4j;

import static com.googlecode.array4j.kernel.Interface.kernel;

import java.nio.ByteBuffer;

public class ByteArray<E extends ByteArray> extends AbstractArray<E> {
    private ByteBuffer fBuffer;

    public ByteArray(final int capacity) {
        this.fBuffer = kernel().createByteBuffer(capacity);
    }

    private void reconfigureBuffer(final int capacity) {
        this.fBuffer = kernel().createByteBuffer(capacity);
    }

    public final byte get(final int... indexes) {
        return fBuffer.get(indexesToIndex(indexes));
    }

    public final E get(final Object... indexes) {
        // TODO check if indexes requires fancy indexing
        parseIndexes(indexes);
        return null;
    }

    public static ByteArray arange(final int stop) {
        return arange(0, stop, 1);
    }

    public static ByteArray arange(final int start, final int stop) {
        return arange(start, stop, 1);
    }

    public static ByteArray arange(final int start, final int stop, final int step) {
        return arange(new ByteArray(0), valueOf(start), valueOf(stop), valueOf(step));
    }

    /**
     * This method takes an instance of E to allow wrappers to pass in an
     * existing instance of be reconfigured.
     */
    protected static <E extends ByteArray> E arange(final E out, final byte start, final byte stop, final byte step) {
        // TODO factor out a common method implemented with longs and then do
        // some conversion to byte, int or short

        // TODO this isn't quite right yet
        int capacity = (stop - start) / step;
        if (capacity < 0) {
            capacity = 0;
        }
        out.reconfigureBuffer(capacity);
        // TODO test for possible overflow problems here
        for (byte x = start; x < stop; x += step) {
            out.fBuffer.put(x);
        }
        out.reconfigureShapeStrides(new int[]{out.fBuffer.capacity()}, new int[]{1});
        return out;
    }

    private static byte valueOf(final int i) {
        if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("integer out of bounds");
        }
        return (byte) i;
    }
}
