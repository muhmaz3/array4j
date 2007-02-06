package com.googlecode.array4j.kernel;

import java.nio.DoubleBuffer;

public final class NativeKernel implements Kernel {
    static {
        System.loadLibrary("nni_blas");
    }

    public static void init() {
    }

    public void fill(final DoubleBuffer in, final double value) {
        in.rewind();
        final int length = in.remaining();
        for (int i = 0; i < length; i++) {
            System.out.println(i);
            in.put(value);
        }
    }

    public void log(final DoubleBuffer in, final DoubleBuffer out) {
        throw new UnsupportedOperationException();
    }

    private static native void log(DoubleBuffer in, DoubleBuffer out, int length);

    public double sum(final DoubleBuffer in) {
        throw new UnsupportedOperationException();
    }

    private static native void sum(DoubleBuffer in, int length);
}
