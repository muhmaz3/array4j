package com.googlecode.array4j.blas;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public abstract class AbstractFloatBLAS implements FloatBLAS {
    private final Storage storage;

    private final Set<Orientation> orientations;

    protected enum Transpose {
        NO_TRANS, TRANS, CONJUGATE_TRANS;
    }

    protected AbstractFloatBLAS(final Storage storage, final Orientation[] orientations) {
        this.storage = storage;
        this.orientations = new HashSet<Orientation>();
        this.orientations.addAll(Arrays.asList(orientations));
    }

    public final void copy(final FloatDenseVector x, final FloatDenseVector y) {
        if (x.size() > y.size()) {
            throw new IllegalArgumentException();
        }
        if (!sameStorage(x, y)) {
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

    public final void gemm(final float alpha, final FloatDenseMatrix a, final FloatDenseMatrix b, final float beta,
            final FloatDenseMatrix c) {
        if (!sameStorage(a, b, c)) {
            throw new IllegalArgumentException("all matrices must have " + a.storage() + " storage");
        }
        if (a.rows() != c.rows()) {
            throw new IllegalArgumentException("rows(a) != rows(c)");
        }
        if (a.columns() != b.rows()) {
            throw new IllegalArgumentException("columns(a) != rows(b)");
        }
        if (b.columns() != c.columns()) {
            throw new IllegalArgumentException("columns(b) != columns(c)");
        }
        if (!orientations.contains(c.orientation())) {
            throw new IllegalArgumentException("unsupported orientation " + a.orientation());
        }
        Transpose transa = c.orientation().equals(a.orientation()) ? Transpose.NO_TRANS : Transpose.TRANS;
        Transpose transb = c.orientation().equals(b.orientation()) ? Transpose.NO_TRANS : Transpose.TRANS;
        gemm0(transa, transb, alpha, a, b, beta, c);
    }

    protected abstract void gemm0(Transpose transa, Transpose transb, float alpha, FloatDenseMatrix a,
            FloatDenseMatrix b, float beta, FloatDenseMatrix c);

    protected abstract float dot0(FloatDenseVector x, FloatDenseVector y);

    private boolean sameStorage(final FloatDenseVector... vectors) {
        for (FloatDenseVector x : vectors) {
            if (!x.storage().equals(storage)) {
                return false;
            }
        }
        return true;
    }

    private boolean sameStorage(final FloatDenseMatrix... matrices) {
        for (FloatDenseMatrix x : matrices) {
            if (!x.storage().equals(storage)) {
                return false;
            }
        }
        return true;
    }

    protected static int ld(final FloatDenseMatrix x) {
        if (Orientation.COLUMN.equals(x.orientation())) {
            return Math.max(1, x.rows());
        } else {
            return Math.max(1, x.columns());
        }
    }
}
