package com.googlecode.array4j;

import com.googlecode.array4j.Indexing.Index;

public class ByteArray<E extends ByteArray> extends AbstractArray<E> {
    public final byte get(final int... indexes) {
        throw new UnsupportedOperationException();
    }

    public final byte get(final Object... indexes) {
        checkIndexes(indexes);
//        throw new UnsupportedOperationException();
        return 0;
    }
}
