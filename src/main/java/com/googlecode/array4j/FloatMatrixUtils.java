package com.googlecode.array4j;

import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import java.util.Random;

// TODO implement unmodifiableMatrix

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

    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V columnSum(final M matrix) {
        final V sum = matrix.createColumnVector();
        for (final V column : matrix.columnsIterator()) {
            sum.plusEquals(column);
        }
        return sum;
    }

    public static FloatDenseVector concatenate(final FloatVector<?>... vectors) {
        if (!sameOrientation(vectors)) {
            throw new IllegalArgumentException("all vectors must have the same orientation");
        }
        int length = 0;
        for (FloatVector<?> vector : vectors) {
            length += vector.length();
        }
        FloatDenseVector output = new FloatDenseVector(length);
        int i = 0;
        for (FloatVector<?> vector : vectors) {
            for (int j = 0; j < vector.length(); j++, i++) {
                output.set(i, vector.get(j));
            }
        }
        return output;
    }

    public static double euclideanDistance(final FloatVector<?> x, final FloatVector<?> y) {
        if (x.length() != y.length()) {
            throw new IllegalArgumentException();
        }
        float distance = 0.0f;
        for (int i = 0; i < x.length(); i++) {
            float d = x.get(i) - y.get(i);
            distance += d * d;
        }
        return Math.sqrt(distance);
    }

    public static void fill(final FloatMatrix<?, ?> matrix, final float value) {
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, value);
            }
        }
    }

    public static void fillGaussian(final FloatMatrix<?, ?> matrix, final double mean, final double stdDev,
            final Random rng) {
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, (float) (mean + (rng.nextGaussian() * stdDev)));
            }
        }
    }

    public static void fillRandom(final FloatMatrix<?, ?> matrix) {
        fillRandom(matrix, new Random());
    }

    public static void fillRandom(final FloatMatrix<?, ?> matrix, final Random rng) {
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, rng.nextFloat());
            }
        }
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

    public static <M extends FloatMatrix<M, V>, V extends FloatVector<V>> V rowSum(final M matrix) {
        final V sum = matrix.createRowVector();
        for (final V row : matrix.rowsIterator()) {
            sum.plusEquals(row);
        }
        return sum;
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

    public static float sum(final FloatMatrix<?, ?> matrix) {
        // TODO can use something from level 1 BLAS to compute this sum
        float sum = 0.0f;
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                sum += matrix.get(i, j);
            }
        }
        return sum;
    }

    public static String toString(final FloatMatrix<?, ?> x) {
        StringBuilder builder = new StringBuilder();
        builder.append(x.rows() + " x " + x.columns());
//        builder.append(", " + orientation + ", " + x.storage());
        builder.append("\n");
        if (x.rows() == 0) {
            builder.append("[]");
            return builder.toString();
        }
        for (int i = 0; i < x.rows(); i++) {
            if (i == 0) {
                builder.append("[");
            } else {
                builder.append(" ");
            }
            builder.append("[");
            for (int j = 0; j < x.columns(); j++) {
                builder.append(String.format("% .15e", x.get(i, j)));
                if (j < x.columns() - 1) {
                    builder.append(" ");
                }
            }
            builder.append("]");
            if (i < x.rows() - 1) {
                builder.append("\n");
            } else {
                builder.append("]");
            }
        }
        return builder.toString();
    }

    public static FloatDenseMatrix zerosLike(final Matrix<?, ?> matrix) {
        return new FloatDenseMatrix(matrix.rows(), matrix.columns());
    }

    private FloatMatrixUtils() {
    }
}
