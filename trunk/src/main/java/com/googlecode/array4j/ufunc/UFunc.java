package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.Array;

public interface UFunc {
    int nin();

    int nout();

    int nargs();

    <E extends Array<E>> E call(E... args);

    // TODO might want this instead, if we support priorities
//    Array2<?> call(Array2<?>... args);

    <E extends Array<E>> E call(Class<E> dtype, Array<?>... args);

    void call(final Array<?>... args);

    <E extends Array<E>> E reduce(E arr);

    <E extends Array<E>, F extends Array<F>> F reduce(E arr, Class<F> dtype);

    <E extends Array<E>> E reduce(E arr, int axis);

    <E extends Array<E>, F extends Array<F>> F reduce(E arr, int axis, Class<F> dtype);

    <E extends Array<E>> E accumulate(E arr);

    <E extends Array<E>, F extends Array<F>> F accumulate(E arr, Class<F> dtype);

    <E extends Array<E>> E accumulate(E arr, int axis);

    <E extends Array<E>, F extends Array<F>> F accumulate(E arr, int axis, Class<F> dtype);

    <E extends Array<E>> E reduceat(final E arr);

    <E extends Array<E>, F extends Array<F>> F reduceat(final E arr, final Class<F> dtype);

    <E extends Array<E>> E reduceat(final E arr, final int axis);

    <E extends Array<E>, F extends Array<F>> F reduceat(final E arr, final int axis, final Class<F> dtype);

    <E extends Array<E>> E reduceat(final E arr, final int[] indices);

    <E extends Array<E>, F extends Array<F>> F reduceat(final E arr, final int[] indices, final Class<F> dtype);

    <E extends Array<E>> E reduceat(final E arr, final int axis, final int... indices);

    <E extends Array<E>, F extends Array<F>> F reduceat(E arr, int axis, int[] indices, Class<F> dtype);

    <E extends Array<E>> E outer(E arr1, E arr2);

    <E extends Array<E>, F extends Array<F>, G extends Array<G>> G outer(E arr1, F arr2, Class<G> dtype);
}
