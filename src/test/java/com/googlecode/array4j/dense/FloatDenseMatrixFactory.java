package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrixFactory;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.util.BufferUtils;
import java.nio.FloatBuffer;

public final class FloatDenseMatrixFactory implements FloatMatrixFactory<FloatDenseMatrix, FloatDenseVector> {
    private final Storage storage;

    public FloatDenseMatrixFactory(final Storage storage) {
        this.storage = storage;
    }

    public FloatDenseMatrix createMatrix(final float[] values, final int rows, final int columns, final int offset,
            final int stride, final Order orientation) {
        FloatBuffer data = BufferUtils.createFloatBuffer(values.length, storage);
        data.put(values);
//        return new FloatDenseMatrix(data, rows, columns, offset, stride, orientation);
        return null;
    }

    public FloatDenseMatrix createMatrix(final float[] values, final int rows, final int columns,
            final Order orientation) {
        return createMatrix(values, rows, columns, 0, 1, orientation);
    }

    public FloatDenseMatrix createMatrix(final int rows, final int columns) {
//        return new FloatDenseMatrix(rows, columns, Order.DEFAULT, storage);
        return null;
    }

    public FloatDenseMatrix createMatrix(final int rows, final int columns, final Order orientation) {
//        return new FloatDenseMatrix(rows, columns, orientation, storage);
        return null;
    }

    public FloatDenseVector createRowVector(final float... values) {
//        return new FloatDenseVector(Order.ROW, storage, values);
        return null;
    }

    public FloatDenseVector createVector(final float[] values, final int size, final int offset, final int stride,
            final Order orientation) {
        FloatBuffer data = BufferUtils.createFloatBuffer(values.length, storage);
        data.put(values);
//        return new FloatDenseVector(data, size, offset, stride, orientation);
        return null;
    }

    public FloatDenseVector createVector(final int size, final Order orientation) {
//        return new FloatDenseVector(size, orientation, storage);
        return null;
    }
}
