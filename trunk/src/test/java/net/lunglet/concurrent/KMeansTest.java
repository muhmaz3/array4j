package net.lunglet.concurrent;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.googlecode.array4j.DenseFloatMatrix;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixUtils;

public final class KMeansTest {
    @Test
    public void testBasic() throws InterruptedException, ExecutionException {
        Random rng = new Random(0);

        KMeans<FloatMatrix<?, ?>> kmeans = KMeans.create();
        DenseFloatMatrix data = new DenseFloatMatrix(10, 5000);
        FloatMatrixUtils.fillRandom(rng, data);
        int maxCentroids = 20;
        FloatMatrix<?, ?> centroids = kmeans.chooseCentroids(maxCentroids, data);
        int iterations = 100;
        centroids = kmeans.train(iterations, centroids, data);
    }
}
