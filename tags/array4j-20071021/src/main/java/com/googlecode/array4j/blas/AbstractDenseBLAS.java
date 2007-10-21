package com.googlecode.array4j.blas;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.dense.DenseMatrix;
import com.googlecode.array4j.dense.DenseVector;

public abstract class AbstractDenseBLAS {
    protected static void checkAxpy(final DenseVector<?> x, final DenseVector<?> y) {
        if (x.length() != y.length()) {
            throw new IllegalArgumentException();
        }
    }

    protected static void checkDot(final DenseVector<?> x, final DenseVector<?> y) {
        if (x.length() != y.length()) {
            throw new IllegalArgumentException();
        }
    }

    protected static void checkGemm(final DenseMatrix<?, ?> a, final DenseMatrix<?, ?> b, final DenseMatrix<?, ?> c) {
        if (a.rows() != c.rows()) {
            throw new IllegalArgumentException(String.format("rows(a)=%d != rows(c)=%d", a.rows(), c.rows()));
        }
        if (a.columns() != b.rows()) {
            throw new IllegalArgumentException(String.format("columns(a)=%d != rows(b)=%d", a.columns(), b.rows()));
        }
        if (b.columns() != c.columns()) {
            throw new IllegalArgumentException(String.format("columns(b)=%d != columns(c)=%d", b.columns(), c.columns()));
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
    protected static int corder(final DenseMatrix<?, ?> c) {
        return c.orientation().equals(Orientation.ROW) ? 101 : 102;
    }

    /**
     * Returns the trans value for the CBLAS interface.
     */
    protected static int ctrans(final DenseMatrix<?, ?> c, final DenseMatrix<?, ?> x) {
        return c.orientation().equals(x.orientation()) ? 111 : 112;
    }

    protected static int leadingDimension(final DenseMatrix<?, ?> x) {
        if (x.orientation().equals(Orientation.COLUMN)) {
            return Math.max(1, x.rows());
        } else {
            return Math.max(1, x.columns());
        }
    }

    protected static String trans(final DenseMatrix<?, ?> c, final DenseMatrix<?, ?> x) {
        return c.orientation().equals(x.orientation()) ? "N" : "T";
    }

    protected final BLASPolicy policy;

    public AbstractDenseBLAS(final BLASPolicy policy) {
        this.policy = policy;
    }
}
