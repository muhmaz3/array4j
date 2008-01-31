package com.googlecode.array4j.blas;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.Direction;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.MatrixTestSupport;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseFactory;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
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

    private static float dot(final FloatVector x, final FloatVector y) {
        assertEquals(x.length(), y.length());
        float ret = 0.0f;
        for (int i = 0; i < x.length(); i++) {
            ret += x.get(i) * y.get(i);
        }
        return ret;
    }

    private static void gemv(final float alpha, final FloatMatrix a, final FloatVector x, final float beta,
            final FloatVector y) {
        gemm(alpha, a, x, beta, y);
    }

    private static void gemm(final float alpha, final FloatMatrix a, final FloatMatrix b, final float beta,
            final FloatMatrix c) {
        assertEquals(a.columns(), b.rows());
        assertEquals(a.rows(), c.rows());
        // TODO figure out how to generalize this assert to work for gemv
//        assertEquals(b.columns(), c.columns());
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
    public void testDot() {
        for (Storage[] s : new Permutations<Storage>(2, Storage.values())) {
            for (int i = 0; i < 10; i++) {
                FloatDenseVector x = DenseFactory.createFloatVector(i, Direction.ROW, s[0]);
                FloatDenseVector y = DenseFactory.createFloatVector(i, Direction.ROW, s[1]);
                MatrixTestSupport.populateMatrix(x);
                MatrixTestSupport.populateMatrix(y);
                assertEquals(dot(x, y), FloatDenseBLAS.DEFAULT.dot(x, y), 0);
            }
        }
    }

    @Test
    public void testGemv() {
        final float alpha = 1.0f;
        final float beta = 1.0f;
        for (Order o : Order.values()) {
            for (Storage[] s : new Permutations<Storage>(3, Storage.values())) {
                for (int m = 0; m < 20; m += m < 5 ? 1 : 5) {
                    for (int n = 1; n < 20; n += n < 5 ? 1 : 5) {
                        FloatDenseMatrix a = DenseFactory.createFloatMatrix(m, n, o, s[0]);
                        FloatDenseVector x = DenseFactory.createFloatVector(n, Direction.COLUMN, s[1]);
                        FloatDenseVector expectedy = DenseFactory.createFloatVector(m, Direction.COLUMN, s[2]);
                        FloatDenseVector actualy = DenseFactory.createFloatVector(m, Direction.COLUMN, s[2]);
                        MatrixTestSupport.populateMatrix(a);
                        MatrixTestSupport.populateMatrix(x);
                        MatrixTestSupport.populateMatrix(expectedy);
                        gemv(alpha, a, x, beta, expectedy);
                        MatrixTestSupport.populateMatrix(actualy);
                        FloatDenseBLAS.DEFAULT.gemv(alpha, a, x, beta, actualy);
                        checkMatrix(expectedy, actualy);
                    }
                }
            }
        }
    }

    @Test
    public void testGemm() {
        final float alpha = 1.0f;
        final float beta = 1.0f;
        for (Order[] o : new Permutations<Order>(3, Order.values())) {
            for (Storage[] s : new Permutations<Storage>(3, Storage.values())) {
                for (int m = 0; m < 20; m += m < 5 ? 1 : 5) {
                    for (int n = 0; n < 20; n += n < 5 ? 1 : 5) {
                        for (int k = 0; k < 20; k += k < 5 ? 1 : 5) {
                            FloatDenseMatrix a = DenseFactory.createFloatMatrix(m, n, o[0], s[0]);
                            FloatDenseMatrix b = DenseFactory.createFloatMatrix(n, k, o[1], s[1]);
                            FloatDenseMatrix expectedc = DenseFactory.createFloatMatrix(m, k, o[2], s[2]);
                            FloatDenseMatrix actualc = DenseFactory.createFloatMatrix(m, k, o[2], s[2]);
                            MatrixTestSupport.populateMatrix(a);
                            MatrixTestSupport.populateMatrix(b);
                            MatrixTestSupport.populateMatrix(expectedc);
                            gemm(alpha, a, b, beta, expectedc);
                            MatrixTestSupport.populateMatrix(actualc);
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
        FloatDenseMatrix a = DenseFactory.createFloatMatrix(2, 3, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(a);
        FloatDenseMatrix expectedc1 = DenseFactory.createFloatMatrix(2, 2, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(expectedc1);
        FloatDenseMatrix actualc1 = DenseFactory.createFloatMatrix(2, 2, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(actualc1);
        FloatDenseMatrix expectedc2 = DenseFactory.createFloatMatrix(3, 3, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(expectedc2);
        FloatDenseMatrix actualc2 = DenseFactory.createFloatMatrix(3, 3, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(actualc2);
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
