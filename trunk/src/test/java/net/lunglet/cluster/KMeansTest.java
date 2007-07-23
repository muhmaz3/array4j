package net.lunglet.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class KMeansTest {
    @Test
    public void testLocalOptimum() throws InterruptedException, ExecutionException {
        int ndata = 3;
        int dimension = 1;
        int maxCentroids = 2;
        Random rng = new Random(1185202905218L);
        KMeans<FloatMatrix<?, ?>> kmeans = KMeans.create();
        FloatDenseMatrix data = new FloatDenseMatrix(dimension, ndata);
        FloatMatrixUtils.fillRandom(rng, data);
        FloatMatrix<?, ?> initialCentroids = kmeans.chooseCentroids(maxCentroids, data);
        double delta = 1.0e-6;
        assertEquals(0.3165713548660278, initialCentroids.get(0, 0), delta);
        assertEquals(0.6212534904479980, initialCentroids.get(0, 1), delta);
        int iterations = 3;
        FloatMatrix<?, ?> centroids = kmeans.train(iterations, initialCentroids, data);
        assertNotSame(initialCentroids, centroids);
        assertEquals(initialCentroids, centroids);
    }

    private void testRandom() throws InterruptedException, ExecutionException {
        KMeans<FloatMatrix<?, ?>> kmeans = KMeans.create();
        while (true) {
            Random rng = new Random(System.currentTimeMillis());
            int ndata = 1000 + rng.nextInt(2000);
            int dimension = 1 + rng.nextInt(10);
            int maxCentroids = 1 + rng.nextInt(10);

            long seed = System.currentTimeMillis();
            rng = new Random(seed);
            System.out.println(String.format("%d %d %d %d", seed, ndata, dimension, maxCentroids));
            FloatDenseMatrix data = new FloatDenseMatrix(dimension, ndata);
            FloatMatrixUtils.fillRandom(rng, data);
            FloatMatrix<?, ?> centroids = kmeans.chooseCentroids(maxCentroids, data);
            int iterations = 100;
            centroids = kmeans.train(iterations, centroids, data);
            System.out.println("-----------");
        }
    }
}
