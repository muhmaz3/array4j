package com.googlecode.array4j.gmm;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.googlecode.array4j.DenseMatrix;
import com.googlecode.array4j.matrix.Vector;

public final class GaussianMixtureTest {
    @Test
    public void testSingleComponent() {
        final DiagonalGaussian gaussian = new DiagonalGaussian(new double[]{0.0}, new double[]{1.0});
        final GaussianMixture gmm = new GaussianMixture(gaussian);
        final double y1 = Math.log(1.0 / Math.sqrt(2.0 * Math.PI));
        assertEquals(y1, gaussian.logLikelihood(0.0));
        assertEquals(y1, gmm.logLikelihood(0.0));
    }

    @Test
    public void testManyComponents() {
        final int dimension = 3;
        final int ndata = 100;
        final double[][] data = new double[ndata][];
        for (int i = 0; i < data.length; i++) {
            data[i] = random(dimension);
        }
        final DenseMatrix dataMat = DenseMatrix.valueOf(data);

        final int mixtures = 7;
        final List<Gaussian> gaussians = new ArrayList<Gaussian>();
        for (int i = 0; i < mixtures; i++) {
            gaussians.add(new DiagonalGaussian(random(dimension), random(dimension)));
        }
        final GaussianMixture gmm = new GaussianMixture(gaussians);
        for (int i = 0; i < 10; i++) {
            final Vector lls = gmm.logLikelihood(dataMat);
        }
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
