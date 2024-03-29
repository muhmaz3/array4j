package net.lunglet.array4j.matrix.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatMatrixUtilsTest {
    @Parameters
    public static Collection<?> data() {
//        return Arrays.asList(new Object[][]{{new FloatDenseMatrixFactory(Storage.HEAP), Order.ROW},
//                {new FloatDenseMatrixFactory(Storage.HEAP), Order.COLUMN},
//                {new FloatDenseMatrixFactory(Storage.DIRECT), Order.ROW},
//                {new FloatDenseMatrixFactory(Storage.DIRECT), Order.COLUMN}});
        return null;
    }

    private final Order order;

    public FloatMatrixUtilsTest(final Order order) {
        this.order = order;
    }

    @Test
    public void testSubMatrixColumns() {
        for (int rows = 0; rows < 5; rows++) {
            for (int columns = 0; columns < 8; columns++) {
                FloatDenseMatrix x = DenseFactory.floatMatrix(rows, columns, order, Storage.HEAP);
                MatrixTestSupport.populateMatrix(x);
                for (int i = 0; i < columns; i++) {
                    for (int j = i; j <= columns; j++) {
                        FloatDenseMatrix y = FloatMatrixUtils.subMatrixColumns(x, i, j);
                        assertEquals(x.rows(), y.rows());
                        assertEquals(j - i, y.columns());
                        for (int k = i; k < j; k++) {
                            int n = k - i;
                            for (int m = 0; m < x.rows(); m++) {
                                assertEquals(x.get(m, k), y.get(m, n), 0);
                            }
                            assertEquals(x.column(k), y.column(n));
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testColumnMean() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
//        FloatMatrix matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, order);
        FloatMatrix matrix = null;
        FloatVector mean = FloatMatrixUtils.columnMean(matrix);
        assertTrue(mean.isColumnVector());
        assertEquals(Direction.COLUMN, mean.direction());
        assertEquals(3, mean.length());
        if (order.equals(Order.ROW)) {
            assertEquals(1.5f, mean.get(0), 0.0);
            assertEquals(3.5f, mean.get(1), 0.0);
            assertEquals(5.5f, mean.get(2), 0.0);
        } else {
            assertEquals(2.5f, mean.get(0), 0.0);
            assertEquals(3.5f, mean.get(1), 0.0);
            assertEquals(4.5f, mean.get(2), 0.0);
        }
    }

    @Test
    public void testColumnSum() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
//        FloatMatrix matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, order);
        FloatMatrix matrix = null;
        FloatVector sum = FloatMatrixUtils.columnSum(matrix);
        assertNotNull(sum);
        assertTrue(sum.isColumnVector());
        if (order.equals(Order.ROW)) {
            assertEquals(3.0f, sum.get(0), 0.0);
            assertEquals(7.0f, sum.get(1), 0.0);
            assertEquals(11.0f, sum.get(2), 0.0);
        } else {
            assertEquals(5.0f, sum.get(0), 0.0);
            assertEquals(7.0f, sum.get(1), 0.0);
            assertEquals(9.0f, sum.get(2), 0.0);
        }
    }

    @Test
    public void testColumnsVector() {
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
//                FloatMatrix x = matrixFactory.createMatrix(i, j, order);
                FloatMatrix x = null;
                MatrixTestSupport.populateMatrix(x);
                FloatDenseVector v = FloatMatrixUtils.columnsVector(x);
                for (int n = 0, k = 0; n < x.columns(); n++) {
                    for (int m = 0; m < x.rows(); m++) {
                        assertEquals(v.get(k++), x.get(m, n), 0);
                    }
                }
            }
        }
    }

    @Test
    public void testConcatenate() {
        Random rng = new Random(0);
        for (int i = 0; i < 5; i++) {
            List<FloatVector> vectors = new ArrayList<FloatVector>();
            for (int j = 0; j < i; j++) {
                int length = rng.nextInt(5);
//                vectors.add(matrixFactory.createVector(length, order.direction()));
            }
            FloatVector[] vecArr = vectors.toArray(new FloatVector[0]);
            FloatDenseVector x = FloatMatrixUtils.concatenate(vecArr);
            for (int m = 0, k = 0; i < vecArr.length; m++) {
                for (int n = 0; n < vecArr[m].length(); n++) {
                    assertEquals(x.get(k++), vecArr[m].get(n), 0);
                }
            }
        }
    }

    @Test
    public void testMean() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
//        FloatMatrix matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, order);
        FloatMatrix matrix = null;
        assertEquals(3.5f, FloatMatrixUtils.mean(matrix), 0.0);
    }

    @Test
    public void testRowMean() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
//        FloatMatrix matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, order);
        FloatMatrix matrix = null;
        FloatVector mean = FloatMatrixUtils.rowMean(matrix);
        assertTrue(mean.isRowVector());
        assertEquals(Direction.ROW, mean.direction());
        assertEquals(2, mean.length());
        if (order.equals(Order.ROW)) {
            assertEquals(3.0f, mean.get(0), 0.0);
            assertEquals(4.0f, mean.get(1), 0.0);
        } else {
            assertEquals(2.0f, mean.get(0), 0.0);
            assertEquals(5.0f, mean.get(1), 0.0);
        }
    }

    @Test
    public void testRowSum() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
//        FloatMatrix matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, order);
        FloatMatrix matrix = null;
        FloatVector sum = FloatMatrixUtils.rowSum(matrix);
        assertNotNull(sum);
        assertTrue(sum.isRowVector());
        if (order.equals(Order.ROW)) {
            assertEquals(9.0f, sum.get(0), 0.0);
            assertEquals(12.0f, sum.get(1), 0.0);
        } else {
            assertEquals(6.0f, sum.get(0), 0.0);
            assertEquals(15.0f, sum.get(1), 0.0);
        }
    }

    @Test
    public void testSum() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
//        FloatMatrix matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, order);
        FloatMatrix matrix = null;
        assertEquals(21.0f, FloatMatrixUtils.sum(matrix), 0.0);
    }
}
