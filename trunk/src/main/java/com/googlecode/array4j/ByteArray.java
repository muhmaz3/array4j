package com.googlecode.array4j;

public interface ByteArray<E extends ByteArray> extends Array<E> {
    byte get(final int... indexes);
}
