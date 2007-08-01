package com.googlecode.array4j.blas;

import org.netlib.blas.Scopy;
import org.netlib.blas.Sdot;
import org.netlib.blas.Sgemm;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class JavaFloatBLAS extends AbstractFloatBLAS {
    private static final FloatBLAS INSTANCE = new JavaFloatBLAS();

    public static FloatBLAS getInstance() {
        return INSTANCE;
    }

    private JavaFloatBLAS() {
        super(Storage.JAVA, new Orientation[]{Orientation.COLUMN});
    }

    @Override
    protected void copy0(final FloatDenseVector x, final FloatDenseVector y) {
        float[] xdata = x.dataArray();
        float[] ydata = y.dataArray();
        Scopy.scopy(x.size(), xdata, x.offset(), x.stride(), ydata, y.offset(), y.stride());
    }

    @Override
    protected float dot0(final FloatDenseVector x, final FloatDenseVector y) {
        float[] xdata = x.dataArray();
        float[] ydata = y.dataArray();
        return Sdot.sdot(x.size(), xdata, x.offset(), x.stride(), ydata, y.offset(), y.stride());
    }

    @Override
    protected void gemm0(final Transpose transa, final Transpose transb, final float alpha, final FloatDenseMatrix a,
            final FloatDenseMatrix b, final float beta, final FloatDenseMatrix c) {
        String transas = transa.equals(Transpose.NO_TRANS) ? "N" : "T";
        String transbs = transb.equals(Transpose.NO_TRANS) ? "N" : "T";
        int m = a.rows();
        int n = b.columns();
        int k = a.columns();
        int lda = ld(a);
        int ldb = ld(b);
        int ldc = ld(c);
        Sgemm.sgemm(transas, transbs, m, n, k, alpha, a.dataArray(), a.offset(), lda, b.dataArray(), b.offset(), ldb,
                beta, c.dataArray(), c.offset(), ldc);
    }
}
