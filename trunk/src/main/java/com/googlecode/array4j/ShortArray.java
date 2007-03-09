package com.googlecode.array4j;

public interface ShortArray<E extends ShortArray> extends Array<E> {
    short get(final int... indexes);
}
