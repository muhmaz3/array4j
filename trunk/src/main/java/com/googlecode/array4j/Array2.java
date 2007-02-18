package com.googlecode.array4j;

public interface Array2<E extends Array2> {
    int[] getShape();

    int getShape(int index);

    int[] shape();

    int shape(int index);

    int getNdim();

    int ndim();

    E reshape(int... shape);

    E getArray(final int... indexes);

    E get(final Object... indexes);
}
