package com.googlecode.array4j.blas;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class FloatDenseBLASTest extends AbstractBLASTest {
    private static void checkMatrix(final FloatDenseMatrix expected, final FloatDenseMatrix actual) {
        assertEquals(expected.rows(), actual.rows());
        assertEquals(expected.columns(), actual.columns());
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.columns(); j++) {
                // compare relative to the reference value because the absolute
                // values can become large
                float refij = expected.get(i, j);
                assertEquals(refij, actual.get(i, j), refij * 1.0e-6f);
            }
        }
    }

    private static void populateMatrix(final FloatMatrix<?, ?> x) {
        for (int i = 0, k = 1; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++, k++) {
                x.set(i, j, (float) k);
            }
        }
    }

    @Test
    public void testGemm() {
        final float alpha = 1.0f;
        final float beta = 1.0f;
        for (Orientation[] o : new Permutations<Orientation>(3, Orientation.values())) {
            for (Storage[] s : new Permutations<Storage>(3, Storage.values())) {
                for (int m = 0; m < 5; m++) {
                    for (int n = 0; n < 5; n++) {
                        for (int k = 0; k < 5; k++) {
                            FloatDenseMatrix a = new FloatDenseMatrix(m, n, o[0], s[0]);
                            FloatDenseMatrix b = new FloatDenseMatrix(n, k, o[1], s[1]);
                            FloatDenseMatrix c1 = new FloatDenseMatrix(m, k, o[2], s[2]);
                            FloatDenseMatrix c2 = new FloatDenseMatrix(m, k, o[2], s[2]);
                            populateMatrix(a);
                            populateMatrix(b);
                            populateMatrix(c1);
                            gemm(alpha, a, b, beta, c1);
                            populateMatrix(c2);
                            FloatDenseBLAS.DEFAULT.gemm(alpha, a, b, beta, c2);
                            checkMatrix(c1, c2);
                        }
                    }
                }
            }
        }
    }

    private static void gemm(final float alpha, final FloatMatrix<?, ?> a, final FloatMatrix<?, ?> b, final float beta,
            final FloatMatrix<?, ?> c) {
        assertEquals(a.columns(), b.rows());
        assertEquals(a.rows(), c.rows());
        assertEquals(b.columns(), c.columns());
        Map<int[], Float> values = new HashMap<int[], Float>();
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < b.columns(); j++) {
                float value = beta * c.get(i, j);
                for (int k = 0; k < a.columns(); k++) {
                    value += alpha * a.get(i, k) * b.get(k, j);
                }
                // don't modify c here, in case it is symmetric
                values.put(new int[]{i, j}, value);
            }
        }
        for (Map.Entry<int[], Float> entry : values.entrySet()) {
            int[] ij = entry.getKey();
            c.set(ij[0], ij[1], entry.getValue());
        }
    }
}
