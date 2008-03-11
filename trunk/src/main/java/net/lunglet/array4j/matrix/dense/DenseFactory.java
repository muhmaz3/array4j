package net.lunglet.array4j.matrix.dense;

import java.nio.FloatBuffer;
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

    public static FloatDenseMatrix createFloatMatrix(final int rows, final int columns) {
        return createFloatMatrix(rows, columns, Order.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseMatrix createFloatMatrix(final int rows, final int columns, final Order order,
            final Storage storage) {
        return new FloatDenseMatrixImpl(rows, columns, order, storage);
    }

    public static FloatDenseVector createFloatVector(final int length) {
        return createFloatVector(length, Direction.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseVector createFloatVector(final int length, final Direction dir) {
        return createFloatVector(length, dir, Storage.DEFAULT);
    }

    public static FloatDenseVector createFloatVector(final int length, final Direction dir, final Storage storage) {
        return new FloatDenseVectorImpl(length, dir, storage);
    }

    public static FloatDenseMatrix valueOf(final float[][] values) {
        return valueOf(values, Order.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseMatrix valueOf(final float[][] values, final Order order, final Storage storage) {
        int m = values.length;
        int n = values.length > 0 ? values[0].length : 0;
        final int rows;
        final int columns;
        if (order.equals(Order.ROW)) {
            rows = m;
            columns = n;
        } else {
            columns = m;
            rows = n;
        }
        FloatDenseMatrix matrix = createFloatMatrix(rows, columns, order, storage);
        FloatBuffer data = matrix.data();
        for (int i = 0; i < m; i++) {
            if (values[i].length != n) {
                throw new IllegalArgumentException();
            }
            data.put(values[i]);
        }
        return matrix;
    }

    private DenseFactory() {
    }
}
