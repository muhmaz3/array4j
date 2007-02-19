package com.googlecode.array4j;

import static com.googlecode.array4j.kernel.Interface.kernel;

import java.nio.ByteBuffer;

public abstract class AbstractByteArray<E extends AbstractByteArray> extends AbstractArray<E> implements ByteArray<E> {
    private ByteBuffer fBuffer;

    protected AbstractByteArray() {
    }

    protected AbstractByteArray(final AbstractByteArray other) {
//        super(other);
        this.fBuffer = other.fBuffer;
    }

    private void reconfigureBuffer(final int capacity) {
        this.fBuffer = kernel().createByteBuffer(capacity);
    }

    public final byte get(final int... indexes) {
        return fBuffer.get(indexesToIndex(indexes));
    }

    public final E get(final Object... indexes) {
        // TODO might be possible to move most of this code into the base calss
        final E arr = create((E) this);
        final AbstractByteArray<E> byteArr = arr;
        final int offset = parseIndexes(indexes);
        byteArr.fBuffer.position(offset);
        byteArr.fBuffer = byteArr.fBuffer.slice();
        return arr;
    }

    protected static <E extends AbstractByteArray> E arange(final int start, final int stop, final int step, final E out) {
        valueOf(start);
        valueOf(stop);
        valueOf(step);

        // TODO factor out a common method implemented with longs and then do
        // some conversion to byte, int or short

        // TODO this isn't quite right yet
        int capacity = (stop - start) / step;
        if (capacity < 0) {
            capacity = 0;
        }
        out.reconfigureBuffer(capacity);
        // TODO test for possible overflow problems here
        for (int x = start; x < stop; x += step) {
            out.fBuffer.put((byte) x);
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
