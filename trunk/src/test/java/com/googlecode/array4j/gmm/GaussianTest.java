package com.googlecode.array4j.gmm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.array4j.gmm.DiagonalGaussian;

public final class GaussianTest {
    @Test
    public void test() {
        final DiagonalGaussian gaussian = new DiagonalGaussian(new double[]{0.0}, new double[]{1.0});
        final double y1 = Math.log(1.0 / Math.sqrt(2.0 * Math.PI));
        assertEquals(y1, gaussian.logLikelihood(0));
    }
}
