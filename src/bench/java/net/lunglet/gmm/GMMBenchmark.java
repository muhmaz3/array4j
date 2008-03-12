package net.lunglet.gmm;

import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.Random;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import org.junit.Ignore;
import org.junit.Test;

public final class GMMBenchmark {
    @Test
    public void benchDiagCovEM() {
        int dimension = 39;
        int mixtures = 16;
        int ndata = 200000;
        Random rng = new Random(0);
        FloatVector weights = DenseFactory.createFloatVector(mixtures);
        FloatMatrixUtils.fill(weights, 1.0f);
        FloatDenseMatrix means = DenseFactory.createFloatMatrix(dimension, mixtures);
        FloatMatrixUtils.fillGaussian(means, 0.0, 4.0, rng);
        FloatDenseMatrix variances = DenseFactory.createFloatMatrix(dimension, mixtures);
        FloatMatrixUtils.fillRandom(variances, 0.2f, 2.0f, rng);
        FloatDenseMatrix data = DenseFactory.createFloatMatrix(dimension, ndata);
        FloatMatrixUtils.fillRandom(data, -10.0f, 10.0f, rng);
        FloatVector[] meansArr = new ArrayList<FloatVector>(means.columnsList()).toArray(new FloatVector[0]);
        FloatVector[] varsArr = new ArrayList<FloatVector>(variances.columnsList()).toArray(new FloatVector[0]);
        DiagCovGMM gmm = new DiagCovGMM(weights, meansArr, varsArr);
        double ll = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 2000; i++) {
            GMMMAPStats stats = new GMMMAPStats(gmm, 0.1);
            stats.add(data.columnsIterator());
            ll = stats.getTotalLogLh();
            assertFalse(Double.isInfinite(ll));
            assertFalse(Double.isNaN(ll));
            System.out.println(ll);
            gmm.doEM(stats);
        }
    }

    @Ignore
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
