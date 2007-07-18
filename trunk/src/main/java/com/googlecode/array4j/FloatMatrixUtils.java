package com.googlecode.array4j;

public final class FloatMatrixUtils {
    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V columnMean(final M matrix) {
        final V mean = matrix.createColumnVector();
        int n = 0;
        for (final V column : matrix.columnsIterator()) {
            n++;
            V delta = column.minus(mean);
            delta.timesEquals(1.0f / n);
            mean.plusEquals(delta);
        }
        return mean;
    }

    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V rowMean(final M matrix) {
        final V mean = matrix.createRowVector();
        int n = 0;
        for (final V row : matrix.rowsIterator()) {
            n++;
            V delta = row.minus(mean);
            delta.timesEquals(1.0f / n);
            mean.plusEquals(delta);
        }
        return mean;
    }

    public static DenseFloatMatrix zerosLike(final Matrix<?, ?> matrix) {
        return new DenseFloatMatrix(matrix.rows(), matrix.columns());
    }

    private FloatMatrixUtils() {
    }
}
