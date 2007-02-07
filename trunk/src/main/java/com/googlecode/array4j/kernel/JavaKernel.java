package com.googlecode.array4j.kernel;

import java.nio.DoubleBuffer;

public final class JavaKernel implements Kernel {
    public static void init() {
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
                final double x = in[i * shape[0] + j] - mean[j];
                out[i] = (x * x) / var[j];
            }
        }
    }
}
