package net.lunglet.model.gmm;

import java.util.Random;
import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import org.junit.Test;

public final class GaussianBenchmark {
    @Test
    public void benchmarkDiagonalGaussian() {
        int dim = 39;
        Order order = Order.COLUMN;
        Storage storage = Storage.DIRECT;
        Direction dir = Direction.COLUMN;
        FloatMatrix data = DenseFactory.createFloatMatrix(dim, 30000, order, storage);
        FloatMatrixUtils.fillGaussian(data, 0.0, 1.0, new Random());
        FloatVector mean = DenseFactory.createFloatVector(dim, dir, storage);
        FloatMatrixUtils.fill(mean, 0.0f);
        FloatVector var = DenseFactory.createFloatVector(dim, dir, storage);
        FloatMatrixUtils.fill(var, 1.0f);
        DiagonalCovarianceGaussian g = new DiagonalCovarianceGaussian(mean, var);
        for (int i = 0; i < 5; i++) {
            for (double ll : g.logLikelihood(data.columnsIterator())) {
                ll = 0;
            }
        }
    }
}
