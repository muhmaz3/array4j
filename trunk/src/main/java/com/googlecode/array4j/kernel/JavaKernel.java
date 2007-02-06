package com.googlecode.array4j.kernel;

import java.nio.DoubleBuffer;

public final class JavaKernel implements Kernel {
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
}
