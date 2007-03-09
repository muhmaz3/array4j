package com.googlecode.array4j;

public interface LongArray<E extends LongArray> extends Array<E> {
    long get(final int... indexes);
}
