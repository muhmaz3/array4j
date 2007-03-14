package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.ArrayDescr;
import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.DenseArray;

/**
 * This class corresponds to the <CODE>PyUFuncObject</CODE> struct in NumPy.
 */
public abstract class AbstractUFunc implements UFunc {
    protected enum Identity {
        ZERO,
        ONE,
        NONE;
    }

    protected static final class Signature {
        private final ArrayType fin1;

        private final ArrayType fin2;

        private final ArrayType fout;

        public Signature(final ArrayType in1, final ArrayType in2, final ArrayType out) {
            this.fin1 = in1;
            this.fin2 = in2;
            this.fout = out;
        }
    }

    private final int fNin;

    private final int fNout;

    private final Identity fIdentity;

//    private final Object[] functions;

    private final Object[] fData;

//    private final boolean checkReturn;

    private final Signature[] fTypes;

//    private Object ptr;

//    private Object obj;

//    private Object[] userloops;

    /**
     * <p>
     * This constructors corresponds to the NumPy function <CODE>PyUFunc_FromFuncAndData</CODE>.
     */
    protected AbstractUFunc(final Object[] data, final Signature[] signatures, final int nin, final int nout,
            final Identity identity) {
        // TODO maybe make AbstractUFunc generic so that we can specify the
        // type of elements in data
        this.fData = data;
        this.fTypes = signatures;
        this.fNin = nin;
        this.fNout = nout;
        this.fIdentity = identity;
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

    /**
     * Generic ufunc call.
     * <p>
     * This function corresponds to the NumPy functions <CODE>ufunc_generic_call</CODE>
     * and <CODE>PyUFunc_GenericFunction</CODE>.
     */
    public final void call(final DenseArray... args) {
        final UFuncLoop loop = new UFuncLoop(this, args);
        loop.execute();
    }

    public final DenseArray call(final ArrayDescr dtype, final DenseArray... args) {
        // TODO create output array from dtype
        // TODO can we have multiple output arrays here?
        // TODO if dtype is null, choose some sensible dtype
        return null;
    }

    public final DenseArray reduce(final DenseArray arr) {
        return reduce(arr, 0, null);
    }

    public final DenseArray reduce(final DenseArray arr, final ArrayDescr dtype) {
        return reduce(arr, 0, dtype);
    }

    public final DenseArray reduce(final DenseArray arr, final int axis) {
        return accumulate(arr, axis, null);
    }

    public final DenseArray reduce(final DenseArray arr, final int axis, final ArrayDescr dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final DenseArray accumulate(final DenseArray arr) {
        return reduce(arr, 0, null);
    }

    public final DenseArray accumulate(final DenseArray arr, final ArrayDescr dtype) {
        return accumulate(arr, 0, dtype);
    }

    public final DenseArray accumulate(final DenseArray arr, final int axis) {
        return accumulate(arr, axis, null);
    }

    public final DenseArray accumulate(final DenseArray arr, final int axis, final ArrayDescr dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final DenseArray reduceat(final DenseArray arr) {
        return reduceat(arr, 0, null, null);
    }

    public final DenseArray reduceat(final DenseArray arr, final ArrayDescr dtype) {
        return reduceat(arr, 0, null, dtype);
    }

    public final DenseArray reduceat(final DenseArray arr, final int axis) {
        return reduceat(arr, axis, null, null);
    }

    public final DenseArray reduceat(final DenseArray arr, final int axis, final ArrayDescr dtype) {
        return reduceat(arr, axis, null, dtype);
    }

    public final DenseArray reduceat(final DenseArray arr, final int[] indices) {
        return reduceat(arr, 0, indices, null);
    }

    public final DenseArray reduceat(final DenseArray arr, final int[] indices, final ArrayDescr dtype) {
        return reduceat(arr, 0, indices, dtype);
    }

    public final DenseArray reduceat(final DenseArray arr, final int axis, final int... indices) {
        return reduceat(arr, axis, indices, null);
    }

    public final DenseArray reduceat(final DenseArray arr, final int axis, final int[] indices, final ArrayDescr dtype) {
        // TODO if dtype is null, make it the same as arr's dtype
        return null;
    }

    public final DenseArray outer(final DenseArray arr1, final DenseArray arr2) {
        return outer(arr1, arr2, arr1.dtype());
    }

    public final DenseArray outer(final DenseArray arr1, final DenseArray arr2, final ArrayDescr dtype) {
        return null;
    }
}
