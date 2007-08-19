package com.googlecode.array4j;

import java.util.Random;

import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

// TODO implement unmodifiableMatrix

public final class FloatMatrixUtils {
    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V rowSum(final M matrix) {
        final V sum = matrix.createRowVector();
        for (final V row : matrix.rowsIterator()) {
            sum.plusEquals(row);
        }
        return sum;
    }

    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V columnSum(final M matrix) {
        final V sum = matrix.createColumnVector();
        for (final V column : matrix.columnsIterator()) {
            sum.plusEquals(column);
        }
        return sum;
    }

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

    public static float sum(final FloatMatrix<?, ?> matrix) {
        // TODO can use something from level 1 BLAS to compute this sum
        // TODO need a way to fetch the BLAS for this matrix. however, the
        // matrix might be sparse or dense, so we will want to call different
        // BLAS functions
        float sum = 0.0f;
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                sum += matrix.get(i, j);
            }
        }
        return sum;
    }

    public static float mean(final FloatMatrix<?, ?> matrix) {
        float mean = 0.0f;
        int n = 0;
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                mean += (matrix.get(i, j) - mean) / ++n;
            }
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

    public static FloatDenseVector concatenate(final FloatVector<?>... vectors) {
        if (!sameOrientation(vectors)) {
            throw new IllegalArgumentException("all vectors must have the same orientation");
        }
        int length = 0;
        for (FloatVector<?> vector : vectors) {
            length += vector.size();
        }
        FloatDenseVector output = new FloatDenseVector(length);
        int i = 0;
        for (FloatVector<?> vector : vectors) {
            for (int j = 0; j < vector.size(); j++, i++) {
                output.set(i, vector.get(j));
            }
        }
        return output;
    }

    private static boolean sameOrientation(final FloatVector<?>... vectors) {
        if (vectors.length == 0) {
            return true;
        }
        boolean isFirstRow = vectors[0].isRowVector();
        for (FloatVector<?> vector : vectors) {
            if (isFirstRow != vector.isRowVector()) {
                return false;
            }
        }
        return true;
    }

    private FloatMatrixUtils() {
    }
}
