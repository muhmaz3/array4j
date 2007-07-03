package com.googlecode.array4j;

import org.junit.Test;

public final class FloatMatrixUtilsTest {
    @Test
    public void testColumMean() {
        DenseFloatMatrix matrix = new DenseFloatMatrix(10, 20);
        DenseFloatVector mean = FloatMatrixUtils.columnMean(matrix);

        DirectFloatMatrix matrix2 = new DirectFloatMatrix(10, 20);
        DirectFloatVector mean2 = FloatMatrixUtils.columnMean(matrix2);
    }
}
