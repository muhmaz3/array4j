package com.googlecode.array4j;

public interface FloatArray<E extends FloatArray> extends Array<E> {
    float get(final int... indexes);
}
