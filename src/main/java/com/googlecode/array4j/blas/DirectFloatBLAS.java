package com.googlecode.array4j.blas;

import java.nio.FloatBuffer;

import com.googlecode.array4j.DirectFloatVector;

public final class DirectFloatBLAS {
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

        public synchronized float sdot(int n, FloatBuffer x, int incx, FloatBuffer y, int incy) {
            return kernel.sdot(n, x, incx, y, incy);
        }
    }

    private static final Kernel KERNEL;

    static {
        String ompNumThreads = System.getenv("OMP_NUM_THREADS");
        if (ompNumThreads != null && Integer.valueOf(ompNumThreads) > 1) {
            KERNEL = new SynchronizedKernel(new KernelImpl());
        } else {
            KERNEL = new KernelImpl();
        }
    }

    public static float dot(final DirectFloatVector x, final DirectFloatVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        return KERNEL.sdot(x.size(), x.getData(), x.stride(), y.getData(), y.stride());
    }

    private DirectFloatBLAS() {
    }
}
