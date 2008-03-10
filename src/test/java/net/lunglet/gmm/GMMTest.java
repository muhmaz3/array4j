package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import org.junit.Test;

public final class GMMTest {
    @Test
    public void testDiagonalCovariance() {
        float[] weights = {1.0f};
        FloatVector[] means = new FloatVector[]{DenseFactory.createFloatVector(1)};
        means[0].set(0, 0.0f);
        FloatVector[] vars = new FloatVector[]{DenseFactory.createFloatVector(1)};
        vars[0].set(0, 1.0f);
        GMM gmm = new DiagCovGMM(weights, means, vars);
        FloatVector x = DenseFactory.createFloatVector(1);
        x.set(0, 0.0f);

        System.out.println(gmm.conditionalLogLh(0, x));
        System.out.println(gmm.jointLogLh(0, x));
    }
}
