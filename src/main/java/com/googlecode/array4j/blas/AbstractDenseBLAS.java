package com.googlecode.array4j.blas;

import com.googlecode.array4j.Order;
import com.googlecode.array4j.matrix.dense.DenseMatrix;
import com.googlecode.array4j.matrix.dense.DenseVector;

public abstract class AbstractDenseBLAS {
    public static final int NOTRANS_INT = 111;

    public static final String NOTRANS_STR = "N";

    public static final int TRANS_INT = 112;

    public static final String TRANS_STR = "T";

    /**
     * Returns the trans value for the CBLAS interface.
     */
    protected static int cblasL3Trans(final DenseMatrix c, final DenseMatrix x) {
        return c.order().equals(x.order()) ? NOTRANS_INT : TRANS_INT;
    }

    protected static void checkAxpy(final DenseVector x, final DenseVector y) {
        if (x.length() != y.length()) {
            throw new IllegalArgumentException();
        }
    }

    protected static void checkDot(final DenseVector x, final DenseVector y) {
        if (x.length() != y.length()) {
            throw new IllegalArgumentException();
        }
    }

    protected static void checkGemm(final DenseMatrix a, final DenseMatrix b, final DenseMatrix c) {
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

    protected static void checkGemv(final DenseMatrix a, final DenseVector x, final DenseVector y) {
        if (a.columns() != x.length()) {
            throw new IllegalArgumentException(String.format("columns(a)=%d != length(x)=%d", a.columns(), x.length()));
        }
        if (a.rows() != y.length()) {
            throw new IllegalArgumentException(String.format("columns(a)=%d != length(y)=%d", a.columns(), y.length()));
        }
        if (a.stride() != 1) {
            throw new IllegalArgumentException("all matrices must have unit stride");
        }
    }

    protected static void checkSyrk(final DenseMatrix a, final DenseMatrix c) {
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
    protected static int corder(final DenseMatrix c) {
        return c.order().equals(Order.ROW) ? 101 : 102;
    }

    /**
     * Returns the trans value for F2J BLAS level 2 functions.
     */
    protected static String f2jL2Order(final DenseMatrix a) {
        return a.order().equals(Order.COLUMN) ? NOTRANS_STR : TRANS_STR;
    }

    protected static String f2jL3Trans(final DenseMatrix c, final DenseMatrix x) {
        return c.order().equals(x.order()) ? NOTRANS_STR : TRANS_STR;
    }

    protected static int leadingDimension(final DenseMatrix x) {
        if (x.order().equals(Order.COLUMN)) {
            return Math.max(1, x.rows());
        } else {
            return Math.max(1, x.columns());
        }
    }

    protected final BLASPolicy policy;

    public AbstractDenseBLAS(final BLASPolicy policy) {
        this.policy = policy;
    }
}
