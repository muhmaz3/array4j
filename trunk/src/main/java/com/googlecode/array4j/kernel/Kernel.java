package com.googlecode.array4j.kernel;

import java.nio.DoubleBuffer;

// TODO also need to send strides?

// TODO also need to send lengths for handling subarrays

public interface Kernel {
    void fill(double value, DoubleBuffer in);

    void log(DoubleBuffer in, DoubleBuffer out);

    double sum(DoubleBuffer in);

    void plus(double value, DoubleBuffer in, DoubleBuffer out);

    void times(double value, DoubleBuffer in, DoubleBuffer out);

    /**
     * Calculate log likelihood of vectors in array.
     *
     * @param shape shape of input (implies size of mean, variance and out)
     * @param meanbuf mean vector of diagonal Gaussian
     * @param varbuf variance vector of diagonal Gaussian
     * @param inbuf buffer containing vectors
     * @param outbuf buffer to contain log likelihood values
     */
    void diagonalLogLikelihood(int[] shape, DoubleBuffer meanbuf, DoubleBuffer varbuf, DoubleBuffer inbuf,
            DoubleBuffer outbuf);
}
