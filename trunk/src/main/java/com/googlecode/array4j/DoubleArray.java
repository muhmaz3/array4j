package com.googlecode.array4j;

public interface DoubleArray<E extends DoubleArray> extends Array2<E> {
    double get(final int... indexes);
}
