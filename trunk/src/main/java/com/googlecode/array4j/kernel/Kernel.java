package com.googlecode.array4j.kernel;

import java.nio.DoubleBuffer;

// TODO also need to send strides?

// TODO also need to send lengths for handling subarrays

public interface Kernel {
    void fill(double value, DoubleBuffer inbuf);

    void log(DoubleBuffer inbuf, DoubleBuffer outbuf);

    double sum(DoubleBuffer inbuf);

    void plus(double value, DoubleBuffer inbuf, DoubleBuffer outbuf);

    void plus(DoubleBuffer inbuf1, DoubleBuffer inbuf2, DoubleBuffer outbuf);

    void times(double value, DoubleBuffer inbuf, DoubleBuffer outbuf);

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
