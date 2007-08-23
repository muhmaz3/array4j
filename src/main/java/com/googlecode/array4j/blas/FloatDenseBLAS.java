package com.googlecode.array4j.blas;

import org.netlib.blas.Sgemm;
import org.netlib.blas.Ssyrk;

import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import com.googlecode.array4j.packed.FloatPackedMatrix;

public final class FloatDenseBLAS extends AbstractDenseBLAS {
    public static float dot(final FloatDenseVector x, final FloatDenseVector y) {
        // TODO move these checks to AbstractDenseBLAS
//        if (x.length() > y.length()) {
//            throw new IllegalArgumentException();
//        }
//        if (!sameStorage(x, y)) {
//            throw new IllegalArgumentException();
//        }
        switch (x.storage()) {
        case HEAP:
            return 1.0f;
        case DIRECT:
            try {
                NATIVE_BLAS_LOCK.lock();
                // TODO call dot in native library using JNA
                // TODO slice buffer at offset before passing to JNA
                return 2.0f;
            } finally {
                NATIVE_BLAS_LOCK.unlock();
            }
        default:
            throw new AssertionError();
        }
    }

    /**
     * <CODE>C = alpha*A*B + beta*C</CODE>.
     */
    public static void gemm(final float alpha, final FloatDenseMatrix a, final FloatDenseMatrix b, final float beta,
            final FloatDenseMatrix c) {
        checkGemm(a, b, c);
        Transpose transa = chooseTrans(c, a);
        Transpose transb = chooseTrans(c, b);
        int m = a.rows();
        int n = b.columns();
        int k = a.columns();
        int lda = ld(a);
        int ldb = ld(b);
        int ldc = ld(c);
        switch (a.storage()) {
        case HEAP:
            Sgemm.sgemm(transa.toString(), transb.toString(), m, n, k, alpha, a.dataArray(), a.offset(), lda,
                    b.dataArray(), b.offset(), ldb, beta, c.dataArray(), c.offset(), ldc);
            return;
        case DIRECT:
            int order = orderForOrientation(c.orientation()).intValue();
            // TODO build these into Transpose
            int transai = transForTranspose(transa).intValue();
            int transbi = transForTranspose(transb).intValue();
            try {
                NATIVE_BLAS_LOCK.lock();
                // TODO call gemm in native library using JNA
                // TODO slice buffer at offset before passing to JNA
                return;
            } finally {
                NATIVE_BLAS_LOCK.unlock();
            }
        default:
            throw new AssertionError();
        }
    }

    public static void sykr(final float alpha, final FloatDenseMatrix a, final float beta, final FloatPackedMatrix c) {
        checkSyrk(a, c);
        Transpose transa = chooseTrans(c, a);
        int n = c.rows();
        int k = a.columns();
        int lda = ld(a);
        int ldc = ld(c);
        switch (a.storage()) {
        case HEAP:
            Ssyrk.ssyrk(c.getUpLo().toString(), transa.toString(), n, k, alpha, a.dataArray(), a.offset(), lda, beta,
                    c.dataArray(), c.offset(), ldc);
        case DIRECT:
            throw new UnsupportedOperationException();
        default:
            throw new AssertionError();
        }
    }
}
