package com.googlecode.array4j.blas;

import com.googlecode.array4j.matrix.dense.FloatDenseMatrix;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.nio.FloatBuffer;
import org.netlib.blas.Saxpy;
import org.netlib.blas.Sdot;
import org.netlib.blas.Sgemm;
import org.netlib.blas.Sgemv;
import org.netlib.blas.Sscal;
import org.netlib.blas.Ssyrk;

// XXX all BLAS level 3 functions require dense matrices

public final class FloatDenseBLAS extends AbstractDenseBLAS {
    public static final FloatDenseBLAS DEFAULT = new FloatDenseBLAS(new BLASPolicy.BestEffort());

    public FloatDenseBLAS(final BLASPolicy policy) {
        super(policy);
    }

    /**
     * <CODE>y := a*x + y</CODE>
     */
    public void axpy(final float a, final FloatDenseVector x, final FloatDenseVector y) {
        checkAxpy(x, y);
        int n = x.length();
        FloatBuffer xbuf = x.data();
        int incx = x.stride();
        FloatBuffer ybuf = y.data();
        int incy = y.stride();
        switch (policy.chooseL1Method(x, y)) {
        case F2J:
            Saxpy.saxpy(n, a, xbuf.array(), xbuf.arrayOffset(), incx, ybuf.array(), ybuf.arrayOffset(), incy);
            return;
        case NATIVE:
            BLASLibrary.INSTANCE.array4j_saxpy(n, a, xbuf, incx, ybuf, incy);
            return;
        default:
            throw new AssertionError();
        }
    }

    public float dot(final FloatDenseVector x, final FloatDenseVector y) {
        checkDot(x, y);
        int n = x.length();
        FloatBuffer xbuf = x.data();
        int incx = x.stride();
        FloatBuffer ybuf = y.data();
        int incy = y.stride();
        switch (policy.chooseL1Method(x, y)) {
        case F2J:
            return Sdot.sdot(n, xbuf.array(), xbuf.arrayOffset(), incx, ybuf.array(), ybuf.arrayOffset(), incy);
        case NATIVE:
            return BLASLibrary.INSTANCE.array4j_sdot(n, xbuf, incx, ybuf, incy);
        default:
            throw new AssertionError();
        }
    }

    /**
     * <CODE>C := alpha*A*B + beta*C</CODE>.
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
        int lda = leadingDimension(a);
        int ldb = leadingDimension(b);
        int ldc = leadingDimension(c);
        switch (policy.chooseL3Method(a, b, c)) {
        case F2J:
            Sgemm.sgemm(f2jL3Trans(c, a), f2jL3Trans(c, b), m, n, k, alpha, abuf.array(), abuf.arrayOffset(), lda,
                bbuf.array(), bbuf.arrayOffset(), ldb, beta, cbuf.array(), cbuf.arrayOffset(), ldc);
            return;
        case NATIVE:
            BLASLibrary.INSTANCE.array4j_sgemm(corder(c), cblasL3Trans(c, a), cblasL3Trans(c, b), m, n, k, alpha, abuf,
                lda, bbuf, ldb, beta, cbuf, ldc);
            return;
        default:
            throw new AssertionError();
        }
    }

    /**
     * <CODE>y := alpha*A*x + beta*y</CODE>.
     */
    public void gemv(final float alpha, final FloatDenseMatrix a, final FloatDenseVector x, final float beta,
            final FloatDenseVector y) {
        checkGemv(a, x, y);
        int m = a.rows();
        int n = a.columns();
        FloatBuffer abuf = a.data();
        FloatBuffer xbuf = x.data();
        FloatBuffer ybuf = y.data();
        int lda = leadingDimension(a);
        int incx = x.stride();
        int incy = y.stride();
        switch (policy.chooseL2Method(a, x, y)) {
        case F2J:
            Sgemv.sgemv(f2jL2Order(a), m, n, alpha, abuf.array(), abuf.arrayOffset(), lda, xbuf.array(),
                xbuf.arrayOffset(), incx, beta, ybuf.array(), ybuf.arrayOffset(), incy);
            return;
        case NATIVE:
            // TODO might want to transpose here in some cases so that ACML works for more cases
            BLASLibrary.INSTANCE.array4j_sgemv(corder(a), NOTRANS_INT, m, n, alpha, abuf, lda, xbuf, incx, beta, ybuf,
                incy);
            return;
        default:
            throw new AssertionError();
        }
    }

    /**
     * <CODE>x = a*x</CODE>
     */
    public void scal(final float a, final FloatDenseVector x) {
        int n = x.length();
        FloatBuffer xbuf = x.data();
        int incx = x.stride();
        switch (policy.chooseL1Method(x, null)) {
        case F2J:
            Sscal.sscal(n, a, xbuf.array(), xbuf.arrayOffset(), incx);
            return;
        case NATIVE:
            BLASLibrary.INSTANCE.array4j_sscal(n, a, xbuf, incx);
            return;
        default:
            throw new AssertionError();
        }
    }

    public void syrk(final float alpha, final FloatDenseMatrix a, final float beta, final FloatDenseMatrix c) {
        checkSyrk(a, c);
        int n = a.rows();
        int k = a.columns();
        FloatBuffer abuf = a.data();
        FloatBuffer cbuf = c.data();
        int lda = leadingDimension(a);
        int ldc = leadingDimension(c);
        switch (policy.chooseL3Method(a, null, c)) {
        case F2J:
            Ssyrk.ssyrk("U", f2jL3Trans(c, a), n, k, alpha, abuf.array(), abuf.arrayOffset(), lda, beta, cbuf.array(),
                cbuf.arrayOffset(), ldc);
            return;
        case NATIVE:
            // typedef enum {CblasUpper=121, CblasLower=122} CBLAS_UPLO;
            BLASLibrary.INSTANCE.array4j_ssyrk(corder(c), 121, cblasL3Trans(c, a), n, k, alpha, abuf, lda, beta, cbuf, ldc);
            return;
        default:
            throw new AssertionError();
        }
    }
}
