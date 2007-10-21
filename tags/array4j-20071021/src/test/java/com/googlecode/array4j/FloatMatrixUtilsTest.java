package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.googlecode.array4j.dense.FloatDenseMatrixFactory;
import com.googlecode.array4j.dense.FloatDenseVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatMatrixUtilsTest<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new FloatDenseMatrixFactory(Storage.HEAP), Orientation.ROW},
                {new FloatDenseMatrixFactory(Storage.HEAP), Orientation.COLUMN},
                {new FloatDenseMatrixFactory(Storage.DIRECT), Orientation.ROW},
                {new FloatDenseMatrixFactory(Storage.DIRECT), Orientation.COLUMN}});
    }

    private final FloatMatrixFactory<M, V> matrixFactory;

    private final Orientation orientation;

    public FloatMatrixUtilsTest(final FloatMatrixFactory<M, V> matrixFactory, final Orientation orientation) {
        this.matrixFactory = matrixFactory;
        this.orientation = orientation;
    }

    @Test
    public void testColumnMean() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        M matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
        V mean = FloatMatrixUtils.columnMean(matrix);
        assertTrue(mean.isColumnVector());
        assertEquals(Orientation.COLUMN, mean.orientation());
        assertEquals(3, mean.length());
        if (orientation.equals(Orientation.ROW)) {
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
        M matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
        V sum = FloatMatrixUtils.columnSum(matrix);
        assertNotNull(sum);
        assertTrue(sum.isColumnVector());
        if (orientation.equals(Orientation.ROW)) {
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
                M x = matrixFactory.createMatrix(i, j, orientation);
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
            List<FloatVector<?>> vectors = new ArrayList<FloatVector<?>>();
            for (int j = 0; j < i; j++) {
                int length = rng.nextInt(5);
                vectors.add(matrixFactory.createVector(length, orientation));
            }
            FloatVector<?>[] vecArr = vectors.toArray(new FloatVector<?>[0]);
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
        M matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
        assertEquals(3.5f, FloatMatrixUtils.mean(matrix), 0.0);
    }

    @Test
    public void testRowMean() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        M matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
        V mean = FloatMatrixUtils.rowMean(matrix);
        assertTrue(mean.isRowVector());
        assertEquals(Orientation.ROW, mean.orientation());
        assertEquals(2, mean.length());
        if (orientation.equals(Orientation.ROW)) {
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
        M matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
        V sum = FloatMatrixUtils.rowSum(matrix);
        assertNotNull(sum);
        assertTrue(sum.isRowVector());
        if (orientation.equals(Orientation.ROW)) {
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
        M matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
        assertEquals(21.0f, FloatMatrixUtils.sum(matrix), 0.0);
    }
}
