package com.googlecode.array4j.fft;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.DoubleArray;

public final class FFTTest {
    @Test
    public void test() {
        final DenseArray x = DoubleArray.arange(10.0);
        final DenseArray y = FFT.fft(x, 10, -1);
        assertNotNull(y);
    }
}
