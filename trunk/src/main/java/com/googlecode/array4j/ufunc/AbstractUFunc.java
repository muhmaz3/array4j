package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.kernel.KernelType;

public abstract class AbstractUFunc implements UFunc {
    private final KernelType fKernelType;

    private final int fNin;

    private final int fNout;

    protected AbstractUFunc(final int nin, final int nout) {
        this(KernelType.DEFAULT, nin, nout);
    }

    protected AbstractUFunc(final KernelType kernelType, final int nin, final int nout) {
        this.fKernelType = kernelType;
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

    public final <E extends Array<E>> E call(final E... args) {
        // TODO check that args.length > 0
        return (E) call(args[0].getClass(), args);
    }

    public final <E extends Array<E>> E call(final Class<E> dtype, final Array<?>... args) {
        // TODO create output array from dtype
        // TODO can we have multiple output arrays here?
        // TODO if dtype is null, choose some sensible dtype
        call(new Array<?>[]{});
        return null;
    }

    public final void call(final Array<?>... args) {
        // TODO split args into input and output argument arrays

        final UFuncLoop loop = new UFuncLoop(this, args);

        // TODO call some code in derived ufunc
//        call(new Array2<?>[]{}, new Array2<?>[]{});
    }

//    protected abstract void call(final Array2<?>[] argsin, final Array2<?>[] argsout);

    public final <E extends Array<E>> E reduce(final E arr) {
        return reduce(arr, 0, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F reduce(final E arr, final Class<F> dtype) {
        return reduce(arr, 0, dtype);
    }

    public final <E extends Array<E>> E reduce(final E arr, final int axis) {
        return accumulate(arr, axis, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F reduce(final E arr, final int axis,
            final Class<F> dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final <E extends Array<E>> E accumulate(final E arr) {
        return reduce(arr, 0, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F accumulate(final E arr, final Class<F> dtype) {
        return accumulate(arr, 0, dtype);
    }

    public final <E extends Array<E>> E accumulate(final E arr, final int axis) {
        return accumulate(arr, axis, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F accumulate(final E arr, final int axis,
            final Class<F> dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final <E extends Array<E>> E reduceat(final E arr) {
        return reduceat(arr, 0, null, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F reduceat(final E arr, final Class<F> dtype) {
        return reduceat(arr, 0, null, dtype);
    }

    public final <E extends Array<E>> E reduceat(final E arr, final int axis) {
        return reduceat(arr, axis, null, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F reduceat(final E arr, final int axis,
            final Class<F> dtype) {
        return reduceat(arr, axis, null, dtype);
    }

    public final <E extends Array<E>> E reduceat(final E arr, final int[] indices) {
        return reduceat(arr, 0, indices, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F reduceat(final E arr, final int[] indices,
            final Class<F> dtype) {
        return reduceat(arr, 0, indices, dtype);
    }

    public final <E extends Array<E>> E reduceat(final E arr, final int axis, final int... indices) {
        return reduceat(arr, axis, indices, null);
    }

    public final <E extends Array<E>, F extends Array<F>> F reduceat(final E arr, final int axis,
            final int[] indices, final Class<F> dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final <E extends Array<E>> E outer(final E arr1, final E arr2) {
        return (E) outer(arr1, arr2, arr1.getClass());
    }

    public final <E extends Array<E>, F extends Array<F>, G extends Array<G>> G outer(final E arr1, final F arr2,
            final Class<G> dtype) {
        return null;
    }
}
