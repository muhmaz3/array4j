package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import org.junit.Test;

public final class GMMBenchmark {
    @Test
    public void benchFullBayesStats() {
        int dimension = 39;
        int mixtures = 2048;
        FloatVector weights = DenseFactory.createFloatVector(mixtures);
        FloatMatrixUtils.fill(weights, 1.0f);
        FloatVector[] means = new FloatVector[mixtures];
        FloatVector mean = DenseFactory.createFloatVector(dimension);
        FloatVector[] variances = new FloatVector[mixtures];
        FloatVector variance = DenseFactory.createFloatVector(dimension);
        FloatMatrixUtils.fill(variance, 1.0f);
        for (int i = 0; i < means.length; i++) {
            means[i] = mean;
            variances[i] = variance;
        }
        float[] x = new float[dimension];
        GMM gmm = new DiagCovGMM(weights, means, variances);
        final double fraction;
        if (false) {
            fraction = GMM.DEFAULT_FRACTION;
        } else {
            fraction = 0.01;
        }
        for (int i = 0; i < 10000; i++) {
            gmm.getStats(x, null, fraction);
        }
    }
}
