package net.lunglet.array4j.matrix.dense;

import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;

public final class DenseFactory {
    public static FloatDenseMatrix copyOf(final FloatMatrix original) {
        return new FloatDenseMatrixImpl(original);
    }

    public static FloatDenseVector copyOf(final FloatVector original) {
        return new FloatDenseVectorImpl(original);
    }

    public static FloatDenseMatrix valueOf(final float[][] values) {
        // TODO use System#arraycopy or FloatBuffer#put(float[]) here if possible
        int rows = values.length;
        int columns = values.length > 0 ? values[0].length : 0;
        FloatDenseMatrix matrix = createFloatMatrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            if (values[i].length != columns) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < columns; j++) {
                matrix.set(i, j, values[i][j]);
            }
        }
        return matrix;
    }

    public static FloatDenseMatrix createFloatMatrix(final int rows, final int columns) {
        return createFloatMatrix(rows, columns, Order.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatDenseMatrix createFloatMatrix(final int rows, final int columns, final Order order,
            final Storage storage) {
        return new FloatDenseMatrixImpl(rows, columns, order, storage);
    }

    public static FloatDenseVector createFloatVector(final int length) {
        return createFloatVector(length, Direction.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatDenseVector createFloatVector(final int length, final Direction dir) {
        return createFloatVector(length, dir, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatDenseVector createFloatVector(final int length, final Direction dir, final Storage storage) {
        return new FloatDenseVectorImpl(length, dir, storage);
    }

    private DenseFactory() {
    }
}
