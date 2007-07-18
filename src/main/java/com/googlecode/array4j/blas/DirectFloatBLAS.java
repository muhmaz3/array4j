package com.googlecode.array4j.blas;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

    // TODO might want to implement this as a dynamic proxy
    private static class SynchronizedKernel implements Kernel {
        private final Kernel kernel;

        public SynchronizedKernel(final Kernel kernel) {
            this.kernel = kernel;
        }

        public synchronized float sdot(final int n, final FloatBuffer x, final int incx, final FloatBuffer y, final int incy) {
            return kernel.sdot(n, x, incx, y, incy);
        }
    }

    private static final int FLOAT_SIZE = Float.SIZE >>> 3;

    public static final FloatBLAS<DirectFloatMatrix, DirectFloatVector> INSTANCE;

    private static final FloatBuffer ONE_BUFFER;

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

    static {
        ONE_BUFFER = createBuffer(1);
        ONE_BUFFER.put(0, 1.0f);
    }

    public static FloatBuffer createBuffer(final int size) {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size * FLOAT_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        return buffer.asFloatBuffer();
    }

    private final Kernel kernel;

    private DirectFloatBLAS(final Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public void axpy(final float value, final DirectFloatVector x, final DirectFloatVector y) {
        throw new UnsupportedOperationException();
    }

    public float dot(final DirectFloatVector x, final DirectFloatVector y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        return kernel.sdot(x.size(), x.data(), x.stride(), y.data(), y.stride());
    }

    @Override
    public int iamax(final DirectFloatVector x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void scal(final float value, final DirectFloatVector x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float sum(final DirectFloatVector x) {
        return kernel.sdot(x.size(), x.data(), x.stride(), ONE_BUFFER, 0);
    }
}
