package com.googlecode.array4j;

public final class FloatMatrixUtils {
    public static <M extends DenseMatrix<M, V> & FloatMatrix<M, V>, V extends DenseVector<V> & FloatVector<V>> V columnMean(M matrix) {
        V mean = matrix.createColumnVector();
        for (V column : matrix.columnsIterator()) {
        }
        return mean;
    }

    private FloatMatrixUtils() {
    }
}
