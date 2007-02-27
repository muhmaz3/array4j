package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

public final class NativeKernel implements Kernel {
    static {
        System.loadLibrary("array4j");
    }

    public static void init() {
    }

    public ByteBuffer createBuffer(final int capacity) {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer;
    }

    public void fill(final double value, final DoubleBuffer in) {
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

    private static native void log(final int length, final DoubleBuffer in, final DoubleBuffer out);

    public double sum(final DoubleBuffer in) {
        throw new UnsupportedOperationException();
    }

    private static native void sum(final int length, final DoubleBuffer in);

    public void plus(final double value, final DoubleBuffer inbuf, final DoubleBuffer outbuf) {
        throw new UnsupportedOperationException();
    }

    public void plus(final DoubleBuffer inbuf1, final DoubleBuffer inbuf2, final DoubleBuffer outbuf) {
        throw new UnsupportedOperationException();
    }

    public void times(final double value, final DoubleBuffer inbuf, final DoubleBuffer outbuf) {
        throw new UnsupportedOperationException();
    }

    public void diagonalLogLikelihood(final int[] shape, final DoubleBuffer meanbuf, final DoubleBuffer varbuf,
            final DoubleBuffer inbuf, final DoubleBuffer outbuf) {
        throw new UnsupportedOperationException();
    }
}
