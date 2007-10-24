package com.googlecode.array4j.blas;

import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import java.util.Random;
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
            Order orient = Order.COLUMN;
            Storage storage = Storage.DIRECT;
//            FloatDenseMatrix a = new FloatDenseMatrix(n, n, orient, storage);
            FloatDenseMatrix a = null;
//            FloatDenseMatrix b = new FloatDenseMatrix(n, n, orient, storage);
            FloatDenseMatrix b = null;
            float beta = 1.0f;
//            FloatDenseMatrix c = new FloatDenseMatrix(n, n, orient, storage);
            FloatDenseMatrix c = null;
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