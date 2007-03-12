package com.googlecode.array4j;

public interface Array<E extends Array> {
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

    void updateFlags(Flags... flags);

    boolean isFortran();

    boolean isWriteable();

    boolean isContiguous();

    E addEquals(Array<?> arr);

//    <F extends Array<?>> F add(Array<?> arr);
}
