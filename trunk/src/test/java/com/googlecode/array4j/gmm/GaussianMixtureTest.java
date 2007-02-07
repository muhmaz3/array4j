package com.googlecode.array4j.gmm;

import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.googlecode.array4j.DenseMatrix;
import com.googlecode.array4j.Vector;

public final class GaussianMixtureTest {
    @Test
    public void test() {
        final int dimension = 39;
        final int ndata = 5 * 60 * 100;
        final double[][] data = new double[ndata][];
        for (int i = 0; i < data.length; i++) {
            data[i] = random(dimension);
        }
        final DenseMatrix dataMat = DenseMatrix.valueOf(data);

        final int mixtures = 10;
        final List<Gaussian> gaussians = new ArrayList<Gaussian>();
        for (int i = 0; i < mixtures; i++) {
            gaussians.add(new DiagonalGaussian(random(dimension), random(dimension)));
        }
        final GaussianMixture gmm = new GaussianMixture(gaussians);

        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            final Vector lls = gmm.logLikelihood(dataMat);
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }

    private double[] random(final int length) {
        final double[] values = new double[length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.random();
        }
        return values;
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(GaussianMixtureTest.class);
    }
}
