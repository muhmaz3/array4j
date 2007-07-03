package com.googlecode.array4j;

public final class FloatMatrixUtils {
    public static FloatVector<?> columnMean(final FloatMatrix<?, ?> matrix) {
        FloatVector<?> mean = matrix.createColumnVector();
        for (FloatVector<?> column : matrix.columnsIterator()) {
        }
        return mean;
    }

    public static float mean(final FloatVector<?> vector) {
        FloatVector<?> transposedVector = vector.transpose();
        FloatVector<?> originalVector = transposedVector.transpose();
        return 0.0f;
    }

    public static FloatVector<?> rowMean(final FloatMatrix<?, ?> matrix) {
        FloatVector<?> mean = matrix.createRowVector();
        for (FloatVector<?> row : matrix.rowsIterator()) {
        }
        return mean;
    }

    private FloatMatrixUtils() {
    }
}
