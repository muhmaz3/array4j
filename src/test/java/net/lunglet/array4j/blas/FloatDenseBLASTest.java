package net.lunglet.array4j.blas;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.MatrixTestSupport;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;
import org.junit.Test;

public final class FloatDenseBLASTest extends AbstractBLASTest {
    private static float dot(final FloatVector x, final FloatVector y) {
        assertEquals(x.length(), y.length());
        float ret = 0.0f;
        for (int i = 0; i < x.length(); i++) {
            ret += x.get(i) * y.get(i);
        }
        return ret;
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

    private static void gemv(final float alpha, final FloatMatrix a, final FloatVector x, final float beta,
            final FloatVector y) {
        gemm(alpha, a, x, beta, y);
    }

    /**
     * Make the matrix symmetric by copying the upper triangular part to the
     * lower triangular part.
     */
    private static <T extends FloatMatrix> T makeSymmetric(final T a) {
        for (int i = 0; i < a.rows(); i++) {
            for (int j = i + 1; j < a.columns(); j++) {
                a.set(j, i, a.get(i, j));
            }
        }
        return a;
    }

    @Test
    public void testDot() {
        for (Storage[] s : new Permutations<Storage>(2, Storage.values())) {
            for (Direction[] d : new Permutations<Direction>(2, Direction.values())) {
                for (int i = 0; i < 10; i++) {
                    FloatDenseVector x = DenseFactory.floatVector(i, d[0], s[0]);
                    FloatDenseVector y = DenseFactory.floatVector(i, d[1], s[1]);
                    MatrixTestSupport.populateMatrix(x);
                    MatrixTestSupport.populateMatrix(y);
                    float z = dot(x, y);
                    assertEquals(z, FloatDenseBLAS.DEFAULT.dot(x, y), 0);
                    FloatDenseVector xt = x.transpose();
                    FloatDenseVector yt = y.transpose();
                    assertEquals(z, FloatDenseBLAS.DEFAULT.dot(xt, yt), 0);
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
                            FloatDenseMatrix a = DenseFactory.floatMatrix(m, n, o[0], s[0]);
                            FloatDenseMatrix b = DenseFactory.floatMatrix(n, k, o[1], s[1]);
                            FloatDenseMatrix expectedc = DenseFactory.floatMatrix(m, k, o[2], s[2]);
                            FloatDenseMatrix actualc = DenseFactory.floatMatrix(m, k, o[2], s[2]);
                            MatrixTestSupport.populateMatrix(a);
                            MatrixTestSupport.populateMatrix(b);
                            MatrixTestSupport.populateMatrix(expectedc);
                            gemm(alpha, a, b, beta, expectedc);
                            MatrixTestSupport.populateMatrix(actualc);
                            FloatDenseBLAS.DEFAULT.gemm(alpha, a, b, beta, actualc);
                            MatrixTestSupport.checkMatrix(expectedc, actualc, 1.0e-6f, true);
                        }
                    }
                }
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
                        FloatDenseMatrix a = DenseFactory.floatMatrix(m, n, o, s[0]);
                        FloatDenseVector x = DenseFactory.floatVector(n, Direction.COLUMN, s[1]);
                        FloatDenseVector expectedy = DenseFactory.floatVector(m, Direction.COLUMN, s[2]);
                        FloatDenseVector actualy = DenseFactory.floatVector(m, Direction.COLUMN, s[2]);
                        MatrixTestSupport.populateMatrix(a);
                        MatrixTestSupport.populateMatrix(x);
                        MatrixTestSupport.populateMatrix(expectedy);
                        gemv(alpha, a, x, beta, expectedy);
                        MatrixTestSupport.populateMatrix(actualy);
                        FloatDenseBLAS.DEFAULT.gemv(alpha, a, x, beta, actualy);
                        MatrixTestSupport.checkMatrix(expectedy, actualy, 1.0e-6f, true);
                    }
                }
            }
        }
    }

    @Test
    public void testScal() {
        for (Storage storage : Storage.values()) {
            for (int i = 0; i < 10; i++) {
                FloatDenseVector x = DenseFactory.floatVector(i, Direction.ROW, storage);
                FloatDenseVector y = DenseFactory.floatVector(i, Direction.ROW, storage);
                MatrixTestSupport.populateMatrix(x);
                MatrixTestSupport.populateMatrix(y);
                FloatDenseBLAS.DEFAULT.scal(1.0f, x);
                for (int j = 0; j < x.length(); j++) {
                    assertEquals(y.get(j), x.get(j), 0);
                }
                FloatDenseBLAS.DEFAULT.scal(2.0f, x);
                for (int j = 0; j < x.length(); j++) {
                    assertEquals(2.0f * y.get(j), x.get(j), 0);
                }
                FloatDenseBLAS.DEFAULT.scal(0, x);
                for (int j = 0; j < x.length(); j++) {
                    assertEquals(0, x.get(j), 0);
                }
            }
        }
    }

    @Test
    public void testSyrk() {
        final float alpha = 1.0f;
        final float beta = 0.0f;
        Storage storage = Storage.DIRECT;
        FloatDenseMatrix a = DenseFactory.floatMatrix(2, 3, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(a);
        FloatDenseMatrix expectedc1 = DenseFactory.floatMatrix(2, 2, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(expectedc1);
        FloatDenseMatrix actualc1 = DenseFactory.floatMatrix(2, 2, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(actualc1);
        FloatDenseMatrix expectedc2 = DenseFactory.floatMatrix(3, 3, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(expectedc2);
        FloatDenseMatrix actualc2 = DenseFactory.floatMatrix(3, 3, Order.COLUMN, storage);
        MatrixTestSupport.populateMatrix(actualc2);
        gemm(alpha, a, a.transpose(), beta, expectedc1);
        gemm(alpha, a.transpose(), a, beta, expectedc2);
        FloatDenseBLAS.DEFAULT.syrk(alpha, a, beta, actualc1);
        // syrk only assigns half of the output matrix, so make it symmetric
        // before checking
        MatrixTestSupport.checkMatrix(expectedc1, makeSymmetric(actualc1), 0);
        FloatDenseBLAS.DEFAULT.syrk(alpha, a.transpose(), beta, actualc2);
        MatrixTestSupport.checkMatrix(expectedc2, makeSymmetric(actualc2), 0);
    }
}
