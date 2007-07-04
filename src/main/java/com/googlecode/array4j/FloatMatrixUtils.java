package com.googlecode.array4j;

public final class FloatMatrixUtils {
    public static <M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>> V columnMean(final M matrix) {
        final V mean = matrix.createColumnVector();
        for (final V column : matrix.columnsIterator()) {
        }
        return mean;
    }

    private FloatMatrixUtils() {
    }
}
