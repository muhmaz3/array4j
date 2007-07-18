package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatMatrixUtilsTest<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new DenseFloatMatrixFactory(), Orientation.ROW},
                {new DenseFloatMatrixFactory(), Orientation.COLUMN}, {new DirectFloatMatrixFactory(), Orientation.ROW},
                {new DirectFloatMatrixFactory(), Orientation.COLUMN}});
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
        assertEquals(3, mean.size());
        if (orientation.equals(Orientation.ROW)) {
            assertEquals(1.5f, mean.get(0));
            assertEquals(3.5f, mean.get(1));
            assertEquals(5.5f, mean.get(2));
        } else {
            assertEquals(2.5f, mean.get(0));
            assertEquals(3.5f, mean.get(1));
            assertEquals(4.5f, mean.get(2));
        }
    }

    @Test
    public void testRowMean() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        M matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
        V mean = FloatMatrixUtils.rowMean(matrix);
        assertTrue(mean.isRowVector());
        assertEquals(Orientation.ROW, mean.orientation());
        assertEquals(2, mean.size());
        if (orientation.equals(Orientation.ROW)) {
            assertEquals(3.0f, mean.get(0));
            assertEquals(4.0f, mean.get(1));
        } else {
            assertEquals(2.0f, mean.get(0));
            assertEquals(5.0f, mean.get(1));
        }
    }
}
