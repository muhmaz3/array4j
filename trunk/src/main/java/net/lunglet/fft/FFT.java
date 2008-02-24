package net.lunglet.fft;

import net.lunglet.array4j.matrix.dense.CFloatDenseVector;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

// TODO do a GSL implementation

public interface FFT {
    CFloatDenseVector fft(FloatDenseVector x, int n);

    // TODO implement matrix fft over rows

    // TODO implement matrix fft over columns

    // TODO implement 2-d fft
}
