package com.googlecode.array4j;

public interface Array<E extends Array> {
    int[] shape();

    int shape(int index);

    int[] strides();

    int strides(int index);

    int size();

    int ndim();

    E reshape(int... shape);

    E getArray(final int... indexes);

    E get(final Object... indexes);

    E addEquals(Array<?> arr);
}
