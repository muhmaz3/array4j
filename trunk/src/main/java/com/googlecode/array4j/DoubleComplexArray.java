package com.googlecode.array4j;

public interface DoubleComplexArray<E extends DoubleComplexArray> extends Array<E> {
    DoubleComplex get(final int... indexes);
}
