package com.googlecode.array4j.cluster;

import junit.framework.JUnit4TestAdapter;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseArray;

public final class KMeansTest {
    @Test
    public void test() {
        final Array data = DenseArray.valueOf(new double[]{1.0}, new double[]{2.0});
        final Array codebook = DenseArray.valueOf(new double[][]{{0.0}});
        final KMeans kmeans = new KMeans(codebook);
        kmeans.train(data, 40, 1.0e-4);
        assertEquals(1.5, codebook.get(0, 0));
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(KMeansTest.class);
    }
}
