package com.googlecode.array4j.blas;

import java.nio.FloatBuffer;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class MKLFloatBLAS extends AbstractFloatBLAS {
    private static final FloatBLAS INSTANCE = new MKLFloatBLAS();

    static {
        System.loadLibrary("array4j");
    }

    private enum CblasConstant {
        ROW_MAJOR(101),
        COL_MAJOR(102),
        NO_TRANS(111),
        TRANS(112),
        CONJ_TRANS(113),
        UPPER(121),
        LOWER(122),
        NONUNIT(131),
        UNIT(132),
        LEFT(141),
        RIGHT(142);

        private final int value;

        CblasConstant(final int value) {
            this.value = value;
        }

        public int intValue() {
            return value;
        }
    }

    public static FloatBLAS getInstance() {
        return INSTANCE;
    }

    private static native void scopy(int n, FloatBuffer x, int offx, int incx, FloatBuffer y, int offy, int incy);

    private static native void gemm(int order, int transa, int transb, int m, int n, int k, float alpha, FloatBuffer a,
            int aoff, int lda, FloatBuffer b, int boff, int ldb, float beta, FloatBuffer c, int coff, int ldc);

    protected MKLFloatBLAS() {
        super(Storage.DIRECT, new Orientation[]{Orientation.ROW, Orientation.COLUMN});
    }

    @Override
    protected void copy0(final FloatDenseVector x, final FloatDenseVector y) {
        scopy(x.size(), x.data(), x.offset(), x.stride(), y.data(), y.offset(), y.stride());
    }

    @Override
    protected float dot0(final FloatDenseVector x, final FloatDenseVector y) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void gemm0(final Transpose transa, final Transpose transb, final float alpha, final FloatDenseMatrix a,
            final FloatDenseMatrix b, final float beta, final FloatDenseMatrix c) {
        int order = orderForOrientation(c.orientation()).intValue();
        int transai = transForTranspose(transa).intValue();
        int transbi = transForTranspose(transb).intValue();
        int m = a.rows();
        int n = b.columns();
        int k = a.columns();
        int lda = ld(a);
        int ldb = ld(b);
        int ldc = ld(c);
        gemm(order, transai, transbi, m, n, k, alpha, a.data(), a.offset(), lda, b.data(), b.offset(), ldb, beta,
                c.data(), c.offset(), ldc);
    }

    private static CblasConstant orderForOrientation(final Orientation orientation) {
        if (Orientation.ROW.equals(orientation)) {
            return CblasConstant.ROW_MAJOR;
        } else {
            return CblasConstant.COL_MAJOR;
        }
    }

    private static CblasConstant transForTranspose(final Transpose trans) {
        switch (trans) {
        case NO_TRANS:
            return CblasConstant.NO_TRANS;
        case TRANS:
            return CblasConstant.TRANS;
        case CONJUGATE_TRANS:
            return CblasConstant.CONJ_TRANS;
        default:
            throw new AssertionError();
        }
    }
}
