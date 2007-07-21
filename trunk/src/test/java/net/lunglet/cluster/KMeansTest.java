package net.lunglet.cluster;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class KMeansTest {
    @Test
    public void testBasic() throws InterruptedException, ExecutionException {
        Random rng = new Random(1184873665080L);
        int ndata = 10000 + rng.nextInt(10000);
        int dimension = 1 + rng.nextInt(20);
        int maxCentroids = 1 + rng.nextInt(20);
        KMeans<FloatMatrix<?, ?>> kmeans = KMeans.create();
        FloatDenseMatrix data = new FloatDenseMatrix(dimension, ndata);
        FloatMatrixUtils.fillRandom(rng, data);
        FloatMatrix<?, ?> centroids = kmeans.chooseCentroids(maxCentroids, data);
        int iterations = 100;
        centroids = kmeans.train(iterations, centroids, data);
    }
}
