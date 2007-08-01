package com.googlecode.array4j;

import java.util.Random;

import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class FloatMatrixUtils {
    private static final int FLOAT_BYTES = Float.SIZE >>> 3;

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

    public static double euclideanDistance(final FloatVector<?> x, final FloatVector<?> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException();
        }
        float distance = 0.0f;
        for (int i = 0; i < x.size(); i++) {
            float d = x.get(i) - y.get(i);
            distance += d * d;
        }
        return Math.sqrt(distance);
    }

    public static void fillRandom(final Random rng, final FloatMatrix<?, ?> x) {
        for (int i = 0; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++) {
                x.set(i, j, rng.nextFloat());
            }
        }
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

    public static FloatDenseMatrix zerosLike(final Matrix<?, ?> matrix) {
        return new FloatDenseMatrix(matrix.rows(), matrix.columns());
    }

    private FloatMatrixUtils() {
    }
}
