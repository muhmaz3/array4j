package com.googlecode.array4j;

public interface IntegerArray<E extends IntegerArray> extends Array<E> {
    int get(final int... indexes);
}
