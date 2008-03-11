package net.lunglet.array4j.matrix.util;

import java.nio.FloatBuffer;
import java.util.Random;
import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.math.FloatMatrixMath;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.Matrix;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.DenseMatrix;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

// TODO implement unmodifiableMatrix

public final class FloatMatrixUtils {
    /** Mean over columns. */
    public static FloatDenseVector columnMean(final FloatMatrix matrix) {
        final FloatDenseVector mean = columnVectorFor(matrix);
        int n = 0;
        for (final FloatVector column : matrix.columnsIterator()) {
            n++;
            FloatVector delta = FloatMatrixMath.minus(column, mean);
            delta.divideEquals(n);
            mean.plusEquals(delta);
        }
        return mean;
    }

    /** Sum over columns. */
    public static FloatDenseVector columnSum(final FloatMatrix matrix) {
        final FloatDenseVector sum = columnVectorFor(matrix);
        for (final FloatVector column : matrix.columnsIterator()) {
            sum.plusEquals(column);
        }
        return sum;
    }

    public static FloatDenseVector columnsVector(final FloatMatrix matrix) {
        int length = matrix.rows() * matrix.columns();
        final FloatDenseVector v;
        if (matrix instanceof DenseMatrix) {
            if (((DenseMatrix) matrix).order().equals(Order.ROW)) {
                throw new UnsupportedOperationException();
            }
            FloatBuffer data = (FloatBuffer) ((DenseMatrix) matrix).data();
//            v = new FloatDenseVector(data, length, 0, 1, Order.COLUMN);
            v = null;
        } else {
            v = DenseFactory.createFloatVector(length, Direction.COLUMN, Storage.DEFAULT);
            for (int i = 0, k = 0; i < matrix.columns(); i++) {
                for (int j = 0; j < matrix.rows(); j++, k++) {
                    v.set(k, matrix.get(j, i));
                }
            }
        }
        return v;
    }

    public static FloatDenseVector concatenate(final FloatVector... vectors) {
        if (!sameDirection(vectors)) {
            throw new IllegalArgumentException("all vectors must have the same direction");
        }
        int length = 0;
        for (FloatVector vector : vectors) {
            length += vector.length();
        }
        FloatDenseVector output = DenseFactory.createFloatVector(length);
        FloatBuffer data = output.data();
        for (FloatVector vector : vectors) {
            for (int j = 0; j < vector.length(); j++) {
                data.put(vector.get(j));
            }
        }
        return output;
    }

    private static FloatDenseVector columnVectorFor(final FloatMatrix matrix) {
        final Storage storage;
        if (matrix instanceof DenseMatrix) {
            storage = ((DenseMatrix) matrix).storage();
        } else {
            storage = Storage.DEFAULT;
        }
        return DenseFactory.createFloatVector(matrix.rows(), Direction.COLUMN, storage);
    }

    private static FloatDenseVector rowVectorFor(final FloatMatrix matrix) {
        final Storage storage;
        if (matrix instanceof DenseMatrix) {
            storage = ((DenseMatrix) matrix).storage();
        } else {
            storage = Storage.DEFAULT;
        }
        return DenseFactory.createFloatVector(matrix.columns(), Direction.ROW, storage);
    }

    public static double euclideanDistance(final FloatVector x, final FloatVector y) {
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

    public static void fill(final FloatMatrix matrix, final float value) {
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, value);
            }
        }
    }

    public static void fillGaussian(final FloatMatrix mat, final double mean, final double stdDev, final Random rng) {
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.columns(); j++) {
                mat.set(i, j, (float) (mean + (rng.nextGaussian() * stdDev)));
            }
        }
    }

    public static void fillRandom(final FloatMatrix matrix) {
        fillRandom(matrix, new Random());
    }

    public static void fillRandom(final FloatMatrix matrix, final Random rng) {
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, rng.nextFloat());
            }
        }
    }

    public static float mean(final FloatMatrix matrix) {
        float mean = 0.0f;
        int n = 0;
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                mean += (matrix.get(i, j) - mean) / ++n;
            }
        }
        return mean;
    }

    /** Mean over rows. */
    public static FloatDenseVector rowMean(final FloatMatrix matrix) {
        final FloatDenseVector mean = rowVectorFor(matrix);
        int n = 0;
        for (final FloatVector row : matrix.rowsIterator()) {
            n++;
            FloatVector delta = FloatMatrixMath.minus(row, mean);
            delta.divideEquals(n);
            mean.plusEquals(delta);
        }
        return mean;
    }

    /** Sum over rows. */
    public static FloatDenseVector rowSum(final FloatMatrix matrix) {
        final FloatDenseVector sum = rowVectorFor(matrix);
        for (final FloatVector row : matrix.rowsIterator()) {
            sum.plusEquals(row);
        }
        return sum;
    }

    public static FloatDenseVector rowsVector(final FloatMatrix matrix) {
        int length = matrix.rows() * matrix.columns();
        FloatDenseVector vec = DenseFactory.createFloatVector(length, Direction.ROW);
        for (int i = 0, k = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++, k++) {
                vec.set(k, matrix.get(i, j));
            }
        }
        return vec;
    }

    private static boolean sameDirection(final FloatVector... vectors) {
        if (vectors.length == 0) {
            return true;
        }
        Direction[] directions = new Direction[vectors.length];
        for (int i = 0; i < vectors.length; i++) {
            directions[i] = vectors[i].direction();
        }
        return directions[0].same(directions);
    }

    public static float sum(final FloatMatrix matrix) {
        // TODO can use something from level 1 BLAS to compute this sum
        float sum = 0.0f;
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                sum += matrix.get(i, j);
            }
        }
        return sum;
    }

    public static String toString(final FloatMatrix x) {
        StringBuilder builder = new StringBuilder();
        builder.append(x.rows() + " x " + x.columns());
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

    public static FloatDenseMatrix zerosLike(final Matrix matrix) {
        return DenseFactory.createFloatMatrix(matrix.rows(), matrix.columns());
    }

    private FloatMatrixUtils() {
    }
}
