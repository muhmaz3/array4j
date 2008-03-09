package net.lunglet.model.gmm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import org.junit.Test;

public final class GaussianTest {
    @Test
    public void testDiagonalCovariance() {
        FloatVector mean = DenseFactory.createFloatVector(1);
        mean.set(0, 0.0f);
        FloatVector var = DenseFactory.createFloatVector(1);
        var.set(0, 1.0f);
        DiagonalCovarianceGaussian g = new DiagonalCovarianceGaussian(mean, var);
        FloatVector x = DenseFactory.createFloatVector(1);
        x.set(0, 0.0f);
        double ll = g.logLikelihood(x);
        System.out.println(ll);
    }
}
