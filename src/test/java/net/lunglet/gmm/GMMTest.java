package net.lunglet.gmm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import org.junit.Ignore;
import org.junit.Test;

public final class GMMTest {
    @Ignore
    @Test
    public void testDiagonalCovariance() {
        FloatVector weights = DenseFactory.valueOf(1.0f);
        FloatVector[] means = new FloatVector[]{DenseFactory.createFloatVector(1)};
        means[0].set(0, 0.0f);
        FloatVector[] vars = new FloatVector[]{DenseFactory.createFloatVector(1)};
        vars[0].set(0, 1.0f);
        GMM gmm = new DiagCovGMM(weights, means, vars);
        FloatVector x = DenseFactory.valueOf(0.0f);
        assertEquals(-0.9189385175, gmm.conditionalLogLh(0, x), 1.0e-6f);
        assertEquals(-0.9189385175, gmm.jointLogLh(0, x), 1.0e-6f);
    }

    @Ignore
    @Test
    public void testDiagCovEMBasic() {
        FloatVector weights = DenseFactory.valueOf(1.0f);
        FloatVector[] means = new FloatVector[]{DenseFactory.createFloatVector(1)};
        means[0].set(0, 2.0f);
        FloatVector[] vars = new FloatVector[]{DenseFactory.createFloatVector(1)};
        vars[0].set(0, 1.0f);
        DiagCovGMM gmm = new DiagCovGMM(weights, means, vars);
        GMMMAPStats stats = new GMMMAPStats(gmm);
        stats.add(DenseFactory.valueOf(-2.0f));
        stats.add(DenseFactory.valueOf(1.0f));
        gmm.doEM(stats);
        assertEquals(-0.5f, gmm.getMean(0).get(0), 0);
        assertEquals(2.25f, gmm.getVariance(0).get(0), 0);
        assertEquals(1.0f, gmm.getWeights().get(0), 0);
        gmm.floorVariances(3.0f);
        assertEquals(3.0f, gmm.getVariance(0).get(0), 0);
    }

    @Test
    public void testDiagCovRandomEM() {
//        int dimension = 22;
//        int mixtures = 100;
//        int ndata = 11;
        int dimension = 4;
        int mixtures = 2;
        int ndata = 3;
        Random rng = new Random(0);
        FloatVector weights = DenseFactory.createFloatVector(mixtures);
        FloatMatrixUtils.fill(weights, 1.0f);
        FloatDenseMatrix means = DenseFactory.createFloatMatrix(dimension, mixtures);
        FloatMatrixUtils.fillGaussian(means, 0.0, 1.0, rng);
        FloatDenseMatrix variances = DenseFactory.createFloatMatrix(dimension, mixtures);
        FloatMatrixUtils.fillRandom(variances, 0.2f, 2.0f, rng);
        FloatDenseMatrix data = DenseFactory.createFloatMatrix(dimension, ndata);
        FloatMatrixUtils.fillRandom(data, -5.0f, 5.0f, rng);
        FloatVector[] meansArr = new ArrayList<FloatVector>(means.columnsList()).toArray(new FloatVector[0]);
        FloatVector[] varsArr = new ArrayList<FloatVector>(variances.columnsList()).toArray(new FloatVector[0]);
        DiagCovGMM gmm = new DiagCovGMM(weights, meansArr, varsArr);

        System.out.println(data);

        double ll = Double.POSITIVE_INFINITY;
        for (int i = 0; i < 20; i++) {
//            printGMM(gmm);
            GMMMAPStats stats = new GMMMAPStats(gmm);
            stats.add(data.columnsIterator());
            double oldll = ll;
            ll = stats.getTotalLogLh();
            assertFalse(Double.isInfinite(ll));
            assertFalse(Double.isNaN(ll));
            System.out.println(oldll + " -> " + ll);
            assertTrue(ll <= oldll);
            gmm.doEM(stats);
        }
    }

    private void printGMM(final GMM gmm) {
        for (int i = 0; i < gmm.getMixtureCount(); i++) {
            System.out.println(gmm.getWeights().get(i));
            System.out.println(gmm.getMean(i));
            System.out.println(gmm.getVariance(i));
            System.out.println("----------------------------------------");
        }
    }

    @Ignore
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        FloatVector weights = DenseFactory.valueOf(1.0f);
        FloatVector[] means = new FloatVector[]{DenseFactory.createFloatVector(1)};
        means[0].set(0, 0.0f);
        FloatVector[] vars = new FloatVector[]{DenseFactory.createFloatVector(1)};
        vars[0].set(0, 1.0f);
        GMM expectedGMM = new DiagCovGMM(weights, means, vars);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(expectedGMM);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        GMM actualGMM = (GMM) ois.readObject();
        assertEquals(expectedGMM.getDimension(), actualGMM.getDimension());
        assertEquals(expectedGMM.getMixtureCount(), actualGMM.getMixtureCount());
        GMMMAPStats stats = new GMMMAPStats(actualGMM);
        stats.add(DenseFactory.valueOf(-2.0f));
        stats.add(DenseFactory.valueOf(1.0f));
        actualGMM.doEM(stats);
        assertEquals(-0.5f, actualGMM.getMean(0).get(0), 0);
        assertEquals(2.25f, actualGMM.getVariance(0).get(0), 0);
        assertEquals(1.0f, actualGMM.getWeights().get(0), 0);
    }
}
