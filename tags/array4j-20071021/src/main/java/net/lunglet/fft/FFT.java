package net.lunglet.fft;

import com.googlecode.array4j.dense.CFloatDenseVector;
import com.googlecode.array4j.dense.FloatDenseVector;

// TODO do a GSL implementation

public interface FFT {
    CFloatDenseVector fft(FloatDenseVector x, int n);

    // TODO implement matrix fft over rows

    // TODO implement matrix fft over columns

    // TODO implement 2-d fft
}
