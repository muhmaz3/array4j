package com.googlecode.array4j.blas;

import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseVector;

public abstract class AbstractFloatBLAS implements FloatBLAS {
    private final Storage storage;

    protected AbstractFloatBLAS(final Storage storage) {
        this.storage = storage;
    }

    /**
     * Perform vector-vector operation <CODE>y = x</CODE>.
     *
     * @param x
     *                source vector
     * @param y
     *                destination vector
     */
    public final void copy(final FloatDenseVector x, final FloatDenseVector y) {
        if (x.size() > y.size()) {
            throw new IllegalArgumentException();
        }
        if (!sameStorage(x, y)) {
            System.out.println(storage);
            System.out.println(x.storage());
            System.out.println(y.storage());
            throw new IllegalArgumentException();
        }
        copy0(x, y);
    }

    protected abstract void copy0(final FloatDenseVector x, final FloatDenseVector y);

    public final float dot(final FloatDenseVector x, final FloatDenseVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        return dot0(x, y);
    }

    protected abstract float dot0(FloatDenseVector x, FloatDenseVector y);

    private boolean sameStorage(final FloatDenseVector... vectors) {
        for (FloatDenseVector x : vectors) {
            if (!x.storage().equals(storage)) {
                return false;
            }
        }
        return true;
    }
}
