package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.Array2;

public interface UFunc {
    int nin();

    int nout();

    int nargs();

    <E extends Array2<E>> E call(E... args);

    <E extends Array2<E>> E call(Class<E> dtype, Array2<?>... args);

    void call(final Array2<?>... args);

    <E extends Array2<E>> E reduce(E arr);

    <E extends Array2<E>, F extends Array2<F>> F reduce(E arr, Class<F> dtype);

    <E extends Array2<E>> E reduce(E arr, int axis);

    <E extends Array2<E>, F extends Array2<F>> F reduce(E arr, int axis, Class<F> dtype);

    <E extends Array2<E>> E accumulate(E arr);

    <E extends Array2<E>, F extends Array2<F>> F accumulate(E arr, Class<F> dtype);

    <E extends Array2<E>> E accumulate(E arr, int axis);

    <E extends Array2<E>, F extends Array2<F>> F accumulate(E arr, int axis, Class<F> dtype);

    <E extends Array2<E>> E reduceat(final E arr);

    <E extends Array2<E>, F extends Array2<F>> F reduceat(final E arr, final Class<F> dtype);

    <E extends Array2<E>> E reduceat(final E arr, final int axis);

    <E extends Array2<E>, F extends Array2<F>> F reduceat(final E arr, final int axis, final Class<F> dtype);

    <E extends Array2<E>> E reduceat(final E arr, final int[] indices);

    <E extends Array2<E>, F extends Array2<F>> F reduceat(final E arr, final int[] indices, final Class<F> dtype);

    <E extends Array2<E>> E reduceat(final E arr, final int axis, final int... indices);

    <E extends Array2<E>, F extends Array2<F>> F reduceat(E arr, int axis, int[] indices, Class<F> dtype);

    <E extends Array2<E>> E outer(E arr1, E arr2);

    <E extends Array2<E>, F extends Array2<F>, G extends Array2<G>> G outer(E arr1, F arr2, Class<G> dtype);
}
