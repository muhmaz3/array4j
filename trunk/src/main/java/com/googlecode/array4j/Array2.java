package com.googlecode.array4j;

public interface Array2<E extends Array2> {
    int[] shape();

    int shape(int index);

    int[] strides();

    int strides(int index);

    int size();

    int nbytes();

    int ndim();

    int flags();

    E reshape(int... shape);

    E getArray(final int... indexes);

    E get(final Object... indexes);

    int elementSize();

    boolean isFortran();

    boolean isWriteable();

    E addEquals(Array2<?> arr);
}
