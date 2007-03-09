package com.googlecode.array4j;

public interface FloatComplexArray<E extends FloatComplexArray> extends Array<E> {
    FloatComplex get(final int... indexes);
}
