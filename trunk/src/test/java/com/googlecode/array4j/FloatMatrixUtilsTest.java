package com.googlecode.array4j;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatMatrixUtilsTest {
    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{new DenseFloatMatrixFactory(), Orientation.ROW},
                {new DenseFloatMatrixFactory(), Orientation.COLUMN}, {new DirectFloatMatrixFactory(), Orientation.ROW},
                {new DirectFloatMatrixFactory(), Orientation.COLUMN}});
    }

    private final FloatMatrixFactory<?, ?> matrixFactory;

    private final Orientation orientation;

    public FloatMatrixUtilsTest(final FloatMatrixFactory<?, ?> matrixFactory, final Orientation orientation) {
        this.matrixFactory = matrixFactory;
        this.orientation = orientation;
    }

    @Test
    public void testColumnMean() {
        float[] values = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
        FloatMatrix<?, ?> matrix = matrixFactory.createMatrix(values, 3, 2, 0, 1, orientation);
    }
}
