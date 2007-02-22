package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.Array2;

public abstract class AbstractUFunc implements UFunc {
    private final int fNin;

    private final int fNout;

    protected AbstractUFunc(final int nin, final int nout) {
        this.fNin = nin;
        this.fNout = nout;
    }

    public final int nin() {
        return fNin;
    }

    public final int nout() {
        return fNout;
    }

    public final int nargs() {
        return fNin + fNout;
    }

    // TODO make some decisions based on the kernel for what to call
    // TODO also allow user to choose a kernel at construction time

    public final void call() {
        // final DenseDoubleArray m1, final DenseDoubleArray m2
//        UFuncLoop loop = new UFuncLoop(this);

        // TODO can probably put this case inside a function of the loop
//        int loopmeth = 0;
//        switch (loopmeth) {
//        case ONE_UFUNCLOOP:
//            break;
//        case NOBUFFER_UFUNCLOOP:
//            break;
//        case BUFFER_UFUNCLOOP:
//            break;
//        default:
//            throw new AssertionError();
//        }
    }

    public final <E extends Array2<E>> E reduce(final E arr) {
        return reduce(arr, 0, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F reduce(final E arr, final Class<F> dtype) {
        return reduce(arr, 0, dtype);
    }

    public final <E extends Array2<E>> E reduce(final E arr, final int axis) {
        return accumulate(arr, axis, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F reduce(final E arr, final int axis,
            final Class<F> dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final <E extends Array2<E>> E accumulate(final E arr) {
        return reduce(arr, 0, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F accumulate(final E arr, final Class<F> dtype) {
        return accumulate(arr, 0, dtype);
    }

    public final <E extends Array2<E>> E accumulate(final E arr, final int axis) {
        return accumulate(arr, axis, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F accumulate(final E arr, final int axis,
            final Class<F> dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final <E extends Array2<E>> E reduceat(final E arr) {
        return reduceat(arr, 0, null, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F reduceat(final E arr, final Class<F> dtype) {
        return reduceat(arr, 0, null, dtype);
    }

    public final <E extends Array2<E>> E reduceat(final E arr, final int axis) {
        return reduceat(arr, axis, null, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F reduceat(final E arr, final int axis,
            final Class<F> dtype) {
        return reduceat(arr, axis, null, dtype);
    }

    public final <E extends Array2<E>> E reduceat(final E arr, final int[] indices) {
        return reduceat(arr, 0, indices, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F reduceat(final E arr, final int[] indices,
            final Class<F> dtype) {
        return reduceat(arr, 0, indices, dtype);
    }

    public final <E extends Array2<E>> E reduceat(final E arr, final int axis, final int... indices) {
        return reduceat(arr, axis, indices, null);
    }

    public final <E extends Array2<E>, F extends Array2<F>> F reduceat(final E arr, final int axis,
            final int[] indices, final Class<F> dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final <E extends Array2<E>, F extends Array2<F>, G extends Array2<G>> G outer(final E arr1, final F arr2,
            final Class<G> dtype) {
        return null;
    }
}
