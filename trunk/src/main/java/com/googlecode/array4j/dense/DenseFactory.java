package com.googlecode.array4j.dense;

import com.googlecode.array4j.Direction;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;

public final class DenseFactory {
    public static FloatDenseMatrix copyOf(final FloatMatrix original) {
        return new FloatDenseMatrixImpl(original);
    }

    public static FloatDenseMatrix createFloatMatrix(final int rows, final int columns) {
        return createFloatMatrix(rows, columns, Order.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatDenseVector createFloatVector(final int length) {
        return createFloatVector(length, Direction.DEFAULT, Storage.DEFAULT_FOR_DENSE);
    }

    private DenseFactory() {
    }

    public static FloatDenseVector createFloatVector(final int length, final Direction dir) {
        return createFloatVector(length, dir, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatDenseVector createFloatVector(final int length, final Direction dir, final Storage storage) {
        return new FloatDenseVectorImpl(length, dir, storage);
    }

    public static FloatDenseMatrix createFloatMatrix(final int rows, final int columns, final Order order,
            final Storage storage) {
        return new FloatDenseMatrixImpl(rows, columns, order, storage);
    }
}
