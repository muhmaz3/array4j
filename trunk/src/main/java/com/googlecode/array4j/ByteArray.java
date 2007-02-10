package com.googlecode.array4j;

import static com.googlecode.array4j.kernel.Interface.kernel;

import java.nio.ByteBuffer;

public class ByteArray<E extends ByteArray> extends AbstractArray<E> {
    private final ByteBuffer fBuffer;

    public ByteArray(final int capacity) {
        this.fBuffer = kernel().createByteBuffer(capacity);
    }

    public final byte get(final int... indexes) {
//        throw new UnsupportedOperationException();
        return 0;
    }

    public final E get(final Object... indexes) {
        checkIndexes(indexes);
//        throw new UnsupportedOperationException();
        return null;
    }
}
