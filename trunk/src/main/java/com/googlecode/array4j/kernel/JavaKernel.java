package com.googlecode.array4j.kernel;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public final class JavaKernel implements Kernel {
    public static void init() {
    }

    public ByteBuffer createByteBuffer(final int capacity) {
        return ByteBuffer.allocate(capacity);
    }

    public ShortBuffer createShortBuffer(final int capacity) {
        return ShortBuffer.allocate(capacity);
    }

    public IntBuffer createIntBuffer(final int capacity) {
        return IntBuffer.allocate(capacity);
    }

    public LongBuffer createLongBuffer(final int capacity) {
        return LongBuffer.allocate(capacity);
    }

    public DoubleBuffer createDoubleBuffer(final int capacity) {
        return DoubleBuffer.allocate(capacity);
    }

    public FloatBuffer createFloatBuffer(final int capacity) {
        return FloatBuffer.allocate(capacity);
    }

    public void fill(final double value, final DoubleBuffer in) {
        // TODO we can use array() here because the buffers aren't direct
        in.rewind();
        final int length = in.remaining();
        for (int i = 0; i < length; i++) {
            in.put(value);
        }
    }

    public void log(final DoubleBuffer in, final DoubleBuffer out) {
        // TODO we can use array() here because the buffers aren't direct
        in.rewind();
        for (int i = 0; i < in.capacity(); i++) {
            // use absolute put here in case in and out are the same buffer
            out.put(i, Math.log(in.get()));
        }
    }

    public double sum(final DoubleBuffer in) {
        double sum = 0.0;
        for (double x : in.array()) {
            sum += x;
        }
        return sum;
    }

    public void plus(final double value, final DoubleBuffer inbuf, final DoubleBuffer outbuf) {
        final double[] in = inbuf.array();
        final double[] out = outbuf.array();
        for (int i = 0; i < in.length; i++) {
            out[i] = value + in[i];
        }
    }

    public void plus(final DoubleBuffer inbuf1, final DoubleBuffer inbuf2, final DoubleBuffer outbuf) {
        final double[] in1 = inbuf1.array();
        final double[] in2 = inbuf2.array();
        final double[] out = outbuf.array();
        for (int i = 0; i < in1.length; i++) {
            out[i] = in1[i] + in2[i];
        }
    }

    public void times(final double value, final DoubleBuffer inbuf, final DoubleBuffer outbuf) {
        final double[] in = inbuf.array();
        final double[] out = outbuf.array();
        for (int i = 0; i < in.length; i++) {
            out[i] = value * in[i];
        }
    }

    public void diagonalLogLikelihood(final int[] shape, final DoubleBuffer meanbuf, final DoubleBuffer varbuf,
            final DoubleBuffer inbuf, final DoubleBuffer outbuf) {
        final double[] mean = meanbuf.array();
        final double[] var = varbuf.array();
        final double[] in = inbuf.array();
        final double[] out = outbuf.array();
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                final double x = in[i * shape[1] + j] - mean[j];
                out[i] = (x * x) / var[j];
            }
        }
    }
}
