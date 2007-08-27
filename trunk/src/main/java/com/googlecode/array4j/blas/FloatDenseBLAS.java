package com.googlecode.array4j.blas;

import java.nio.FloatBuffer;

import org.netlib.blas.Sgemm;

import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class FloatDenseBLAS extends AbstractDenseBLAS {
    public static final FloatDenseBLAS DEFAULT = new FloatDenseBLAS(new BLASPolicy.BestEffort());

    public FloatDenseBLAS(final BLASPolicy policy) {
        super(policy);
    }

    /**
     * <CODE>C = alpha * A * B + beta * C</CODE>.
     */
    public void gemm(final float alpha, final FloatDenseMatrix a, final FloatDenseMatrix b, final float beta,
            final FloatDenseMatrix c) {
        checkGemm(a, b, c);
        int m = a.rows();
        int n = b.columns();
        int k = a.columns();
        FloatBuffer abuf = a.data();
        FloatBuffer bbuf = b.data();
        FloatBuffer cbuf = c.data();
        int lda = ld(a);
        int ldb = ld(b);
        int ldc = ld(c);
        switch (policy.chooseL3Method(a, b, c)) {
        case F2J:
            Sgemm.sgemm(trans(c, a), trans(c, b), m, n, k, alpha, abuf.array(), abuf.arrayOffset(), lda, bbuf.array(),
                bbuf.arrayOffset(), ldb, beta, cbuf.array(), cbuf.arrayOffset(), ldc);
            return;
        case NATIVE:
            try {
                NATIVE_BLAS_LOCK.lock();
                NativeBLASLibrary.INSTANCE.array4j_sgemm(corder(c), ctrans(c, a), ctrans(c, b), m, n, k, alpha, abuf,
                    lda, bbuf, ldb, beta, cbuf, ldc);
                return;
            } finally {
                NATIVE_BLAS_LOCK.unlock();
            }
        default:
            throw new AssertionError();
        }
    }
}
