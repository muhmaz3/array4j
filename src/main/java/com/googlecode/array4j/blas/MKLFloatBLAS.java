package com.googlecode.array4j.blas;

import java.nio.FloatBuffer;

import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class MKLFloatBLAS extends AbstractFloatBLAS {
    private static final FloatBLAS INSTANCE = new MKLFloatBLAS();

    static {
        System.loadLibrary("array4j");
    }

    public static FloatBLAS getInstance() {
        return INSTANCE;
    }

    private static native void scopy(int n, FloatBuffer x, int offx, int incx, FloatBuffer y, int offy, int incy);

    protected MKLFloatBLAS() {
        super(Storage.DIRECT);
    }

    @Override
    protected void copy0(final FloatDenseVector x, final FloatDenseVector y) {
        scopy(x.size(), x.data(), x.offset(), x.stride(), y.data(), y.offset(), y.stride());
    }

    @Override
    protected float dot0(final FloatDenseVector x, final FloatDenseVector y) {
        throw new UnsupportedOperationException();
    }
}
