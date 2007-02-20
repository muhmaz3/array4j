package com.googlecode.array4j;

public interface Array2<E extends Array2> {
    int[] shape();

    int shape(int index);

    int ndim();

    E reshape(int... shape);

    E getArray(final int... indexes);

    E get(final Object... indexes);

    int elementSize();
}
