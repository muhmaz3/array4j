package com.googlecode.array4j;

import org.junit.Test;

public final class FloatMatrixUtilsTest {
    @Test
    public void testColumMean() {
        final DenseFloatMatrix matrix = new DenseFloatMatrix(10, 20);
        final DenseFloatVector mean = FloatMatrixUtils.columnMean(matrix);

        final DirectFloatMatrix matrix2 = new DirectFloatMatrix(10, 20);
        final DirectFloatVector mean2 = FloatMatrixUtils.columnMean(matrix2);
    }
}
