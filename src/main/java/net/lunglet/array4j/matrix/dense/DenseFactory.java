package net.lunglet.array4j.matrix.dense;

import java.nio.FloatBuffer;
import java.util.Collection;
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

    public static FloatDenseMatrix createFloatMatrix(final int[] dims, final Order order, final Storage storage) {
        if (dims.length != 2) {
            throw new IllegalArgumentException();
        }
        return new FloatDenseMatrixImpl(dims[0], dims[1], order, storage);
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

    /**
     * Returns a copy of the vector with direct storage.
     */
    public static FloatDenseVector directCopy(final FloatVector other) {
        FloatDenseVector v = new FloatDenseVectorImpl(other.length(), other.direction(), Storage.DIRECT);
        v.data().put(other.toArray());
        return v;
    }

    public static FloatDenseVector directRowVector(final int length) {
        return createFloatVector(length, Direction.ROW, Storage.DIRECT);
    }

    public static FloatDenseVector floatVector(final Collection<? extends Number> values) {
        FloatDenseVector v = createFloatVector(values.size());
        int i = 0;
        for (Number x : values) {
            v.set(i++, x.floatValue());
        }
        return v;
    }

    /**
     * Returns a copy of the vector with heap storage.
     */
    public static FloatDenseVector heapCopy(final FloatVector other) {
        FloatDenseVector v = new FloatDenseVectorImpl(other.length(), other.direction(), Storage.HEAP);
        v.data().put(other.toArray());
        return v;
    }

    public static FloatDenseVector valueOf(final float... values) {
        return valueOf(values, Direction.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseVector valueOf(final float[] values, final Direction dir, final Storage storage) {
        FloatDenseVector v = createFloatVector(values.length, dir, storage);
        v.data().put(values);
        return v;
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
