package com.googlecode.array4j.gmm;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public final class DiagonalGaussianTest {
    @Test
    public void test() {
        final DiagonalGaussian gaussian = new DiagonalGaussian(new double[]{0.0}, new double[]{1.0});
        final double y1 = Math.log(1.0 / Math.sqrt(2.0 * Math.PI));
        assertEquals(y1, gaussian.logLikelihood(0.0));
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagonalGaussianTest.class);
    }
}
