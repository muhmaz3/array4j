package com.googlecode.array4j;

public final class FloatMatrixUtils {
    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V columnMean(final M matrix) {
        final V mean = matrix.createRowVector();
        for (final V column : matrix.columnsIterator()) {
//            mean.addEquals(column);
        }
        return mean;
    }

    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V rowMean(final M matrix) {
        final V mean = matrix.createColumnVector();
        for (final V row : matrix.rowsIterator()) {
//            mean.addEquals(row);
        }
        return mean;
    }

    private FloatMatrixUtils() {
    }
}
