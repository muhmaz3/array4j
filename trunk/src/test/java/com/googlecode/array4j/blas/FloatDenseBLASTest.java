package com.googlecode.array4j.blas;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.TestSupport;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

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

    @Test
    public void testGemm() {
        final float alpha = 1.0f;
        final float beta = 1.0f;
        for (Orientation[] o : new Permutations<Orientation>(3, Orientation.values())) {
            for (Storage[] s : new Permutations<Storage>(3, Storage.values())) {
                for (int m = 0; m < 20; m += m < 5 ? 1 : 5) {
                    for (int n = 0; n < 20; n += n < 5 ? 1 : 5) {
                        for (int k = 0; k < 20; k += k < 5 ? 1 : 5) {
                            FloatDenseMatrix a = new FloatDenseMatrix(m, n, o[0], s[0]);
                            FloatDenseMatrix b = new FloatDenseMatrix(n, k, o[1], s[1]);
                            FloatDenseMatrix expectedc = new FloatDenseMatrix(m, k, o[2], s[2]);
                            FloatDenseMatrix actualc = new FloatDenseMatrix(m, k, o[2], s[2]);
                            TestSupport.populateMatrix(a);
                            TestSupport.populateMatrix(b);
                            TestSupport.populateMatrix(expectedc);
                            gemm(alpha, a, b, beta, expectedc);
                            TestSupport.populateMatrix(actualc);
                            FloatDenseBLAS.DEFAULT.gemm(alpha, a, b, beta, actualc);
                            checkMatrix(expectedc, actualc);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testSyrk() {
        final float alpha = 1.0f;
        final float beta = 0.0f;
        Storage storage = Storage.DIRECT;
        FloatDenseMatrix a = new FloatDenseMatrix(2, 3, Orientation.COLUMN, storage);
        TestSupport.populateMatrix(a);
        FloatDenseMatrix expectedc1 = new FloatDenseMatrix(2, 2, Orientation.COLUMN, storage);
        TestSupport.populateMatrix(expectedc1);
        FloatDenseMatrix actualc1 = new FloatDenseMatrix(2, 2, Orientation.COLUMN, storage);
        TestSupport.populateMatrix(actualc1);
        FloatDenseMatrix expectedc2 = new FloatDenseMatrix(3, 3, Orientation.COLUMN, storage);
        TestSupport.populateMatrix(expectedc2);
        FloatDenseMatrix actualc2 = new FloatDenseMatrix(3, 3, Orientation.COLUMN, storage);
        TestSupport.populateMatrix(actualc2);
        gemm(alpha, a, a.transpose(), beta, expectedc1);
        gemm(alpha, a.transpose(), a, beta, expectedc2);

        System.out.println(a);
        System.out.println(expectedc1);
        System.out.println(expectedc2);

        FloatDenseBLAS.DEFAULT.syrk(alpha, a, beta, actualc1);
        System.out.println(actualc1);
        FloatDenseBLAS.DEFAULT.syrk(alpha, a.transpose(), beta, actualc2);
        System.out.println(actualc2);
    }
}
