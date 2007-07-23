package com.googlecode.array4j;

import java.nio.FloatBuffer;

import org.netlib.blas.Scopy;
import org.netlib.blas.Sdot;

import com.googlecode.array4j.dense.FloatDenseVector;

public final class FloatBLAS {
    static {
        // TODO try to detect from MKL DLL using MKLGetVersion
        System.loadLibrary("array4j");
    }

    /**
     * Perform vector-vector operation <CODE>y = x</CODE>.
     *
     * @param x
     *                source vector
     * @param y
     *                destination vector
     */
    public static void copy(final FloatDenseVector x, final FloatDenseVector y) {
        if (x.size() > y.size()) {
            throw new IllegalArgumentException();
        }
        if (sameStorage(Storage.JAVA, x, y)) {
            float[] xdata = x.dataAsArray();
            float[] ydata = y.dataAsArray();
            Scopy.scopy(x.size(), xdata, x.offset(), x.stride(), ydata, y.offset(), y.stride());
        } else if (sameStorage(Storage.DIRECT, x, y)) {
            scopy(x.size(), x.data(), x.offset(), x.stride(), y.data(), y.offset(), y.stride());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static float dot(final FloatDenseVector x, final FloatDenseVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        if (sameStorage(Storage.JAVA, x, y)) {
            float[] xdata = x.dataAsArray();
            float[] ydata = y.dataAsArray();
            return Sdot.sdot(x.size(), xdata, x.offset(), x.stride(), ydata, y.offset(), y.stride());
        } else if (sameStorage(Storage.DIRECT, x, y)) {
            throw new UnsupportedOperationException();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static boolean sameStorage(final Storage storage, final FloatDenseVector... vectors) {
        for (FloatDenseVector x : vectors) {
            if (!x.storage().equals(storage)) {
                return false;
            }
        }
        return true;
    }

    private static native void scopy(int n, FloatBuffer x, int offx, int incx, FloatBuffer y, int offy, int incy);

    private static native float sdot(int n, FloatBuffer x, int offx, int incx, FloatBuffer y, int offy, int incy);

    private FloatBLAS() {
    }
}
