package net.lunglet.gmm;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        float[] weights = {1.0f};
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
    }
}
