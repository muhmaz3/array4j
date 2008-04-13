package net.lunglet.gmm;

import java.util.ArrayList;
import java.util.Random;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import net.lunglet.gmm.GMMFixer.GMMFixerStats;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GMMFixerTest {
    private final Logger logger = LoggerFactory.getLogger(GMMFixerTest.class);

    @Test
    public void test() {
        int dimension = 39;
        int mixtures = 20;
        int ndata = 1000000;
        Random rng = new Random(0);
        FloatVector weights = DenseFactory.floatVector(mixtures);
        FloatMatrixUtils.fill(weights, 1.0f);
        FloatDenseMatrix means = DenseFactory.floatMatrix(dimension, mixtures);
        FloatMatrixUtils.fillGaussian(means, 0.0, 4.0, rng);
        FloatDenseMatrix variances = DenseFactory.floatMatrix(dimension, mixtures);
        FloatMatrixUtils.fillRandom(variances, 0.2f, 2.0f, rng);
        FloatDenseMatrix data = DenseFactory.floatMatrix(dimension, ndata);
        FloatMatrixUtils.fillRandom(data, -10.0f, 10.0f, rng);
        FloatVector[] meansArr = new ArrayList<FloatVector>(means.columnsList()).toArray(new FloatVector[0]);
        FloatVector[] varsArr = new ArrayList<FloatVector>(variances.columnsList()).toArray(new FloatVector[0]);
        DiagCovGMM gmm = new DiagCovGMM(weights, meansArr, varsArr);
        boolean fixerEnabled = true;
        logger.info("Is GMMFixer enabled? {}", fixerEnabled);
        for (int iter = 1; iter <= 2000; iter++) {
            GMMMAPStats stats = new GMMMAPStats(gmm, 0.01);
            stats.add(data.columnsIterator());
            double ll = stats.getTotalLogLh();
            logger.info("iteration {}, log likelihood = {}", iter, ll);
            gmm.doEM(stats);
            if (fixerEnabled && iter % 20 == 0) {
                GMMFixer gmmFixer = new GMMFixer(gmm, stats.getN(), 3);
                for (int fixiter = 1; fixiter < 5; fixiter++) {
                    logger.info("fixer iteration {}", fixiter);
                    GMMFixerStats globalStats = gmmFixer.createStats();
                    GMMFixerStats stats1 = gmmFixer.add(data.columnsIterator());
                    GMMFixer.addStats(globalStats, stats1);
                    gmmFixer.doEM(globalStats);
                }
                gmm = gmmFixer.done();
            }
        }
    }
}
