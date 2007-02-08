package com.googlecode.array4j.cluster;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.googlecode.array4j.DenseMatrix;
import com.googlecode.array4j.Matrix;

public final class KMeansTest {
    @Test
    public void test() {
        final Matrix data = DenseMatrix.valueOf(new double[]{1.0}, new double[]{2.0});
        final Matrix codebook = DenseMatrix.valueOf(new double[][]{{0.0}});
        final KMeans kmeans = new KMeans(codebook);
        kmeans.train(data, 40, 1.0e-4);
        assertEquals(1.5, codebook.get(0, 0));
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(KMeansTest.class);
    }
}
