package com.googlecode.array4j.blas;

import java.nio.FloatBuffer;

import com.googlecode.array4j.DirectFloatMatrix;
import com.googlecode.array4j.DirectFloatVector;

public final class DirectFloatBLAS implements FloatBLAS<DirectFloatMatrix, DirectFloatVector> {
    private interface Kernel {
        float sdot(int n, FloatBuffer x, int incx, FloatBuffer y, int incy);
    }

    private static class KernelImpl implements Kernel {
        static {
            System.loadLibrary("array4j");
        }

        public native float sdot(int n, FloatBuffer x, int incx, FloatBuffer y, int incy);
    }

    private static class SynchronizedKernel implements Kernel {
        private final Kernel kernel;

        public SynchronizedKernel(final Kernel kernel) {
            this.kernel = kernel;
        }

        public synchronized float sdot(final int n, final FloatBuffer x, final int incx, final FloatBuffer y, final int incy) {
            return kernel.sdot(n, x, incx, y, incy);
        }
    }

    public static final DirectFloatBLAS INSTANCE;

    static {
        final String ompNumThreads = System.getenv("OMP_NUM_THREADS");
        final Kernel kernel;
        if (ompNumThreads != null && Integer.valueOf(ompNumThreads) > 1) {
            kernel = new SynchronizedKernel(new KernelImpl());
        } else {
            kernel = new KernelImpl();
        }
        INSTANCE = new DirectFloatBLAS(kernel);
    }

    private final Kernel kernel;

    private DirectFloatBLAS(final Kernel kernel) {
        this.kernel = kernel;
    }

    public float dot(final DirectFloatVector x, final DirectFloatVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        return kernel.sdot(x.size(), x.getData(), x.stride(), y.getData(), y.stride());
    }
}
