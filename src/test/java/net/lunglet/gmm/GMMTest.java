package net.lunglet.gmm;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import org.junit.Test;

public final class GMMTest {
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
    public void testDiagCovVarianceFlooring() {
        FloatVector weights = DenseFactory.valueOf(1.0f);
        FloatVector[] means = new FloatVector[]{DenseFactory.createFloatVector(4)};
        FloatVector[] vars = new FloatVector[]{DenseFactory.createFloatVector(4)};
        DiagCovGMM gmm = new DiagCovGMM(weights, means, vars);
        FloatVector varFloor = DenseFactory.valueOf(0.01f, 0.01f, 0.01f, 0.01f);
        gmm.floorVariances(0.001f);
        gmm.floorVariances(varFloor);
    }

    @Test
    public void testDiagonalCovariance() {
        FloatVector weights = DenseFactory.valueOf(1.0f);
        FloatVector[] means = new FloatVector[]{DenseFactory.createFloatVector(1)};
        means[0].set(0, 1.0f);
        FloatVector[] vars = new FloatVector[]{DenseFactory.createFloatVector(1)};
        vars[0].set(0, 1.0f);
        GMM gmm = new DiagCovGMM(weights, means, vars);
        FloatVector x = DenseFactory.valueOf(0.0f);
        assertEquals(-1.41893853320467, gmm.marginalLogLh(x), 1.0e-6f);
    }

    @Test
    public void testGMMMAPStats() {
        FloatMatrix data = DenseFactory.valueOf(new float[][]{{-2.0f, 2.0f}});
        FloatVector weights = DenseFactory.valueOf(0.5f, 0.5f);
        FloatVector[] means = new FloatVector[]{DenseFactory.valueOf(1.0f), DenseFactory.valueOf(-1.0f)};
        FloatVector[] vars = new FloatVector[]{DenseFactory.valueOf(1.0f), DenseFactory.valueOf(1.0f)};
        DiagCovGMM gmm = new DiagCovGMM(weights, means, vars);
        GMMMAPStats stats = new GMMMAPStats(gmm);
        stats.add(data.rowsIterator());

        GMMMAPStats stats2 = new GMMMAPStats(gmm);
        List<int[]> indices = stats2.add(data.rowsIterator(), 2);
        assertEquals(data.rows(), indices.size());
        assertEquals(stats.getTotalLogLh(), stats2.getTotalLogLh(), 0);

        GMMMAPStats stats3 = new GMMMAPStats(gmm);
        stats3.add(data.rowsIterator(), indices);
        assertEquals(stats.getTotalLogLh(), stats3.getTotalLogLh(), 0);

        stats2 = new GMMMAPStats(gmm);
        indices = stats2.add(data.rowsIterator(), 1);
        assertEquals(data.rows(), indices.size());
        stats3 = new GMMMAPStats(gmm);
        stats3.add(data.rowsIterator(), indices);
        assertEquals(stats2.getTotalLogLh(), stats3.getTotalLogLh(), 0);
    }

    @Test
    public void testMAPonMeans() {
        FloatMatrix data = DenseFactory.valueOf(new float[][]{{-2.0f, 2.0f}});
        FloatVector weights = DenseFactory.valueOf(0.5f, 0.5f);
        FloatVector[] means = new FloatVector[]{DenseFactory.valueOf(1.0f), DenseFactory.valueOf(-1.0f)};
        FloatVector[] vars = new FloatVector[]{DenseFactory.valueOf(1.0f), DenseFactory.valueOf(1.0f)};
        DiagCovGMM ubm = new DiagCovGMM(weights, means, vars);
        int c = 1;
        GMMMAPStats ubmStats = new GMMMAPStats(ubm);
        List<int[]> indices = ubmStats.add(data.rowsIterator(), c);
        assertEquals(data.rows(), indices.size());

        DiagCovGMM gmm = ubm.copy();
        GMMMAPStats stats = new GMMMAPStats(gmm);
        stats.add(data.rowsIterator(), indices);
        gmm.doMAPonMeans(stats, 0.0f);
        assertEquals(2.0f, gmm.getMean(0).get(0), 0);
        assertEquals(-2.0f, gmm.getMean(1).get(0), 0);

        float r = 1.0f;
        gmm = ubm.copy();
        stats = new GMMMAPStats(gmm);
        stats.add(data.rowsIterator(), indices);
        gmm.doMAPonMeans(stats, r);
        assertEquals(1.5f, gmm.getMean(0).get(0), 0);
        assertEquals(-1.5f, gmm.getMean(1).get(0), 0);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        FloatVector weights = DenseFactory.valueOf(1.0f);
        FloatVector[] means = new FloatVector[]{DenseFactory.createFloatVector(1)};
        means[0].set(0, 0.0f);
        FloatVector[] vars = new FloatVector[]{DenseFactory.createFloatVector(1)};
        vars[0].set(0, 1.0f);
        DiagCovGMM expectedGMM = new DiagCovGMM(weights, means, vars);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(expectedGMM);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        DiagCovGMM actualGMM = (DiagCovGMM) ois.readObject();
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
