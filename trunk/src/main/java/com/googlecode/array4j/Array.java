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

    double getDouble(int... indexes);

    int getInteger(int... indexes);

    E addEquals(Array<?> arr);

    E multiplyEquals(Array<?> arr);

    E squareEquals();

    E sqrtEquals();

    E ldexpEquals(Array<?> arr);

    E logEquals();

    E expEquals();
}
