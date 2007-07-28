package com.googlecode.array4j.blas;

import org.netlib.blas.Scopy;
import org.netlib.blas.Sdot;

import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class JavaFloatBLAS extends AbstractFloatBLAS {
    private static final FloatBLAS INSTANCE = new JavaFloatBLAS();

    public static FloatBLAS getInstance() {
        return INSTANCE;
    }

    private JavaFloatBLAS() {
        super(Storage.JAVA);
    }

    @Override
    protected void copy0(final FloatDenseVector x, final FloatDenseVector y) {
        float[] xdata = x.dataAsArray();
        float[] ydata = y.dataAsArray();
        Scopy.scopy(x.size(), xdata, x.offset(), x.stride(), ydata, y.offset(), y.stride());
    }

    @Override
    protected float dot0(final FloatDenseVector x, final FloatDenseVector y) {
        float[] xdata = x.dataAsArray();
        float[] ydata = y.dataAsArray();
        return Sdot.sdot(x.size(), xdata, x.offset(), x.stride(), ydata, y.offset(), y.stride());
    }
}
