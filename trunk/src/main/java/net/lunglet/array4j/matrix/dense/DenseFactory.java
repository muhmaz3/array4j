package net.lunglet.array4j.matrix.dense;

import java.nio.FloatBuffer;
import java.util.Collection;
import net.lunglet.array4j.Direction;
import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;

/**
 * Factory for creating dense matrices and vectors.
 */
public final class DenseFactory {
    public static FloatDenseMatrix copyOf(final FloatMatrix original) {
        return new FloatDenseMatrixImpl(original);
    }

    public static FloatDenseVector copyOf(final FloatVector original) {
        return new FloatDenseVectorImpl(original);
    }

    /**
     * Returns a copy of the vector with direct storage.
     */
    public static FloatDenseVector directCopyOf(final FloatVector other) {
        FloatDenseVector v = new FloatDenseVectorImpl(other.length(), other.direction(), Storage.DIRECT);
        v.data().put(other.toArray());
        return v;
    }

    /**
     * Returns a column vector of floats with the specified length.
     */
    public static FloatVector floatColumn(final int length) {
        return floatVector(length, Direction.COLUMN, Storage.DEFAULT);
    }

    /**
     * Returns a column vector of floats with the specified length and direct
     * storage.
     */
    public static FloatDenseVector floatColumnDirect(final int length) {
        return floatVector(length, Direction.COLUMN, Storage.DIRECT);
    }

    public static FloatDenseMatrix floatMatrix(final float[][] values) {
        return floatMatrix(values, Order.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseMatrix floatMatrix(final float[][] values, final Order order, final Storage storage) {
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
        FloatDenseMatrix matrix = floatMatrix(rows, columns, order, storage);
        FloatBuffer data = matrix.data();
        for (int i = 0; i < m; i++) {
            if (values[i].length != n) {
                throw new IllegalArgumentException();
            }
            data.put(values[i]);
        }
        return matrix;
    }

    /**
     * Returns a matrix of floats with the specified number of rows and columns.
     */
    public static FloatDenseMatrix floatMatrix(final int rows, final int columns) {
        return floatMatrix(rows, columns, Order.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseMatrix floatMatrix(final int rows, final int columns, final Order order,
            final Storage storage) {
        return new FloatDenseMatrixImpl(rows, columns, order, storage);
    }

    public static FloatDenseMatrix floatMatrix(final int[] dims, final Order order, final Storage storage) {
        if (dims.length != 2) {
            throw new IllegalArgumentException();
        }
        return floatMatrix(dims[0], dims[1], order, storage);
    }

    /**
     * Returns a row vector of floats with the specified length.
     */
    public static FloatDenseVector floatRow(final int length) {
        return floatVector(length, Direction.ROW, Storage.DEFAULT);
    }

    /**
     * Returns a row vector of floats with the specified length and direct
     * storage.
     */
    public static FloatDenseVector floatRowDirect(final int length) {
        return floatVector(length, Direction.ROW, Storage.DIRECT);
    }

    /**
     * Returns a row matrix of floats with the specified dimensions and direct
     * storage.
     */
    public static FloatDenseMatrix floatRowDirect(final int[] dims) {
        return floatMatrix(dims, Order.ROW, Storage.DIRECT);
    }

    /**
     * Returns a row matrix of floats with the specified rows and columns and
     * heap storage.
     */
    public static FloatDenseMatrix floatRowHeap(final int rows, final int columns) {
        return floatMatrix(rows, columns, Order.ROW, Storage.HEAP);
    }

    public static FloatDenseVector floatVector(final Collection<? extends Number> values) {
        FloatDenseVector v = floatVector(values.size(), Direction.DEFAULT, Storage.DEFAULT);
        int i = 0;
        for (Number x : values) {
            v.set(i++, x.floatValue());
        }
        return v;
    }

    public static FloatDenseVector floatVector(final double[] values) {
        FloatDenseVector v = floatVector(values.length, Direction.DEFAULT, Storage.DEFAULT);
        for (int i = 0; i < values.length; i++) {
            v.set(i, (float) values[i]);
        }
        return v;
    }

    /**
     * Returns a vector of floats containing the specified values.
     */
    public static FloatDenseVector floatVector(final float... values) {
        return floatVector(values, Direction.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseVector floatVector(final float[] values, final Direction dir, final Storage storage) {
        FloatDenseVector v = floatVector(values.length, dir, storage);
        v.data().put(values);
        return v;
    }

    public static FloatDenseVector floatVector(final int length) {
        return floatVector(length, Direction.DEFAULT, Storage.DEFAULT);
    }

    public static FloatDenseVector floatVector(final int length, final Direction dir, final Storage storage) {
        return new FloatDenseVectorImpl(length, dir, storage);
    }

    /**
     * Returns a copy of the vector with heap storage.
     */
    public static FloatDenseVector heapCopyOf(final FloatVector other) {
        FloatDenseVector v = new FloatDenseVectorImpl(other.length(), other.direction(), Storage.HEAP);
        v.data().put(other.toArray());
        return v;
    }

    private DenseFactory() {
    }
}
