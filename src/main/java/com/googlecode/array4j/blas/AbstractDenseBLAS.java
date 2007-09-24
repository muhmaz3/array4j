package com.googlecode.array4j.blas;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.dense.DenseMatrix;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public abstract class AbstractDenseBLAS {
    protected static void checkDot(final FloatDenseVector x, final FloatDenseVector y) {
        if (x.length() != y.length()) {
            throw new IllegalArgumentException();
        }
    }

    protected static void checkGemm(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b, final DenseMatrix<?, ?> c) {
        if (a.rows() != c.rows()) {
            throw new IllegalArgumentException("rows(a) != rows(c)");
        }
        if (a.columns() != b.rows()) {
            throw new IllegalArgumentException("columns(a) != rows(b)");
        }
        if (b.columns() != c.columns()) {
            throw new IllegalArgumentException("columns(b) != columns(c)");
        }
        if (a.stride() != 1 || b.stride() != 1 || c.stride() != 1) {
            throw new IllegalArgumentException("all matrices must have unit stride");
        }
    }

    protected static void checkSyrk(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> c) {
        if (a.rows() != c.rows()) {
            throw new IllegalArgumentException("rows(a) != rows(c)");
        }
        if (a.rows() != c.columns()) {
            throw new IllegalArgumentException("rows(a) != columns(c)");
        }
        if (a.stride() != 1 || c.stride() != 1) {
            throw new IllegalArgumentException("all matrices must have unit stride");
        }
    }

    /**
     * Returns the order value for the CBLAS interface.
     */
    protected static int corder(final FloatDenseMatrix c) {
        return c.orientation().equals(Orientation.ROW) ? 101 : 102;
    }

    /**
     * Returns the trans value for the CBLAS interface.
     */
    protected static int ctrans(final FloatDenseMatrix c, final FloatDenseMatrix x) {
        return c.orientation().equals(x.orientation()) ? 111 : 112;
    }

    // TODO this ld doesn't take submatrices into account yet
    protected static int ld(final FloatDenseMatrix x) {
        if (x.orientation().equals(Orientation.COLUMN)) {
            return Math.max(1, x.rows());
        } else {
            return Math.max(1, x.columns());
        }
    }

    protected static String trans(final FloatDenseMatrix c, final FloatDenseMatrix x) {
        return c.orientation().equals(x.orientation()) ? "N" : "T";
    }

    protected final BLASPolicy policy;

    public AbstractDenseBLAS(final BLASPolicy policy) {
        this.policy = policy;
    }
}
