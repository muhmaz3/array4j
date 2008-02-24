package net.lunglet.array4j.blas;

import java.util.Random;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.blas.FloatDenseBLAS;
import net.lunglet.array4j.math.FloatMatrixUtils;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import org.junit.Test;

public final class FloatDenseBLASBenchmark {
    @Test
    public void benchmarkGemm() {
        Random rng = new Random(0);
        for (int n = 50; n < 751; n += 10) {
            final int r;
            if (n < 100) {
                r = 5000;
            } else {
                r = 500;
            }
            float alpha = 1.0f;
            Order order = Order.COLUMN;
            Storage storage = Storage.DIRECT;
            FloatDenseMatrix a = DenseFactory.createFloatMatrix(n, n, order, storage);
            FloatDenseMatrix b = DenseFactory.createFloatMatrix(n, n, order, storage);
            float beta = 1.0f;
            FloatDenseMatrix c = DenseFactory.createFloatMatrix(n, n, order, storage);
            FloatMatrixUtils.fillRandom(a, rng);
            FloatMatrixUtils.fillRandom(b, rng);
            FloatMatrixUtils.fillRandom(c, rng);
            for (int i = 0; i < 10; i++) {
                FloatDenseBLAS.DEFAULT.gemm(alpha, a, b, beta, c);
            }
            long startTime = System.nanoTime();
            for (int i = 0; i < r; i++) {
                FloatDenseBLAS.DEFAULT.gemm(alpha, a, b, beta, c);
            }
            long t = System.nanoTime() - startTime;
            int f = 2 * (n + 1) * n * n;
            double mfs = f / (t / 1000.0) * r;
            System.out.println(String.format("n = %d, mfs = %f", n, mfs));
        }
    }
}
