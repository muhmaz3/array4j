package com.googlecode.array4j.kernel;

import java.nio.DoubleBuffer;

// TODO also need to send strides?

// TODO also need to send lengths for handling subarrays

public interface Kernel {
    void fill(DoubleBuffer in, double value);

    void log(DoubleBuffer in, DoubleBuffer out);

    double sum(DoubleBuffer in);
}
