package com.googlecode.array4j.dense;

import com.googlecode.array4j.ComplexFloat;
import com.googlecode.array4j.ComplexFloatMatrixFactory;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.util.BufferUtils;
import java.nio.FloatBuffer;

public final class CFloatDenseMatrixFactory implements ComplexFloatMatrixFactory<CFloatDenseMatrix, CFloatDenseVector> {
    private final Storage storage;

    public CFloatDenseMatrixFactory(final Storage storage) {
        this.storage = storage;
    }

    public CFloatDenseMatrix createMatrix(final float[] values, final int rows, final int columns, final int offset,
            final int stride, final Order orientation) {
        FloatBuffer data = BufferUtils.createComplexFloatBuffer(values.length, storage);
        data.put(values);
//        return new CFloatDenseMatrix(data, rows, columns, offset, stride, orientation);
        return null;
    }

    public CFloatDenseMatrix createMatrix(final float[] values, final int rows, final int columns,
            final Order orientation) {
        return createMatrix(values, rows, columns, 0, 1, orientation);
    }

    public CFloatDenseMatrix createMatrix(final int rows, final int columns) {
//        return new CFloatDenseMatrix(rows, columns, Order.DEFAULT, storage);
        return null;
    }

    public CFloatDenseMatrix createMatrix(final int rows, final int columns, final Order orientation) {
//        return new CFloatDenseMatrix(rows, columns, orientation, storage);
        return null;
    }

    public CFloatDenseVector createRowVector(final ComplexFloat... values) {
//        return new CFloatDenseVector(Order.ROW, storage, values);
        return null;
    }

    public CFloatDenseVector createVector(final float[] values, final int size, final int offset, final int stride,
            final Order orientation) {
        FloatBuffer data = BufferUtils.createComplexFloatBuffer(values.length, storage);
        data.put(values);
//        return new CFloatDenseVector(data, size, offset, stride, orientation);
        return null;
    }

    public CFloatDenseVector createVector(final int size, final Order orientation) {
//        return new CFloatDenseVector(size, orientation, storage);
        return null;
    }
}
