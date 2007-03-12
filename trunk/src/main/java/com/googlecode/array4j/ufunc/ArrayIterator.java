package com.googlecode.array4j.ufunc;

import java.util.Iterator;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.Flags;

/**
 * Iterator for elements in an N-dimensional array.
 * <p>
 * Corresponds to <CODE>PyArrayIterObject</CODE> in the NumPy C-API.
 */
public final class ArrayIterator implements Iterable<ArrayIterator> {
    /* number of dimensions - 1 */
    private int ndm1;

    private int index;

    private int size;

    /* N-dimensional loop */
    private int[] coordinates;

    /* ao->dimensions - 1 */
    private int[] dimsm1;

    /* ao->strides or fake */
    private int[] strides;

    /* how far to jump back */
    private int[] backstrides;

    /* shape factors */
    private int[] factors;

    private final Array<?> ao;

    /* pointer to current item */
    private int dataptr;

    private boolean contiguous;

    /**
     * Constructor.
     * <p>
     * This code corresponds to the <CODE>PyArray_IterNew</CODE> function in NumPy.
     */
    public ArrayIterator(final Array<?> arr) {
        this.ao = arr;
        ao.updateFlags(Flags.CONTIGUOUS);
        contiguous = ao.isContiguous();
        size = ao.size();
        final int nd = ao.ndim();
        ndm1 = nd - 1;
        factors = new int[nd];
        factors[nd - 1] = 1;
        dimsm1 = new int[nd];
        strides = new int[nd];
        backstrides = new int[nd];
        // TODO might be better to call ao.shape() and then index into it
        for (int i = 0; i < nd; i++) {
            dimsm1[i] = ao.shape(i) - 1;
            strides[i] = ao.strides(i);
            backstrides[i] = strides[i] * dimsm1[i];
            if (i > 0) {
                factors[nd - i - 1] = factors[nd - i] * ao.shape(nd - i);
            }
        }
        // TODO check if this array size is right
        coordinates = new int[nd];
        reset();
    }

    public int ndim() {
        return ao.ndim();
    }

    public int shape(final int index) {
        return ao.shape(index);
    }

    // TODO next

    // TODO goto

    // TODO goto1d

    /**
     * Reset iterator.
     * <p>
     * This code corresponds to the <CODE>PyArray_ITER_RESET</CODE> macro in NumPy.
     */
    public void reset() {
        index = 0;
        // reset dataptr to start of array
        dataptr = 0;
        // TODO reseting coordinates to 0 might be faster than allocating a new array
        coordinates = new int[coordinates.length];
    }

    /**
     * Reset the iterator dimensions and strides of each iterator object using 0
     * valued strides for broadcasting.
     * <p>
     * This code is based on the second part of the <CODE>PyArray_Broadcast</CODE>
     * function in NumPy.
     */
    public void resetAfterBroadcast(final MultiArrayIterator mit) {
        final int tmp = mit.size();
        final int mitnd = mit.ndim();
        ndm1 = mitnd - 1;
        size = tmp;
        final int nd = ao.ndim();

        factors = resizeCopy(mitnd, factors);
        dimsm1 = resizeCopy(mitnd, dimsm1);
        strides = resizeCopy(mitnd, strides);
        backstrides = resizeCopy(mitnd, backstrides);

        factors[mitnd - 1] = 1;
        for (int j = 0; j < mitnd; j++) {
            dimsm1[j] = mit.shape(j) - 1;
            final int k = j + nd - mitnd;
            /*
             * If this dimension was added or shape of underlying array was 1
             */
            if ((k < 0) || ao.shape(k) != mit.shape(j)){
                contiguous = false;
                strides[j] = 0;
            } else {
                strides[j] = ao.strides(k);
            }
            backstrides[j] = strides[j] * dimsm1[j];
            if (j > 0) {
                final int idx = mitnd - j;
                factors[idx - 1] = factors[idx] * mit.shape(idx);
            }
        }
        reset();
    }

    public Iterator<ArrayIterator> iterator() {
        return null;
    }

    private int[] resizeCopy(final int length, final int[] oldarr) {
        if (length == oldarr.length) {
            return oldarr;
        }
        final int[] newarr = new int[length];
        System.arraycopy(oldarr, 0, newarr, 0, oldarr.length);
        return newarr;
    }
}
