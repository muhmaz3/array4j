package com.googlecode.array4j.dense;

import java.nio.FloatBuffer;

import com.googlecode.array4j.ComplexFloat;
import com.googlecode.array4j.ComplexFloatMatrixFactory;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;

public final class CFloatDenseMatrixFactory implements ComplexFloatMatrixFactory<CFloatDenseMatrix, CFloatDenseVector> {
    private final Storage storage;

    public CFloatDenseMatrixFactory(final Storage storage) {
        this.storage = storage;
    }

    public CFloatDenseMatrix createMatrix(final float[] values, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        FloatBuffer data = AbstractCFloatDense.createFloatBuffer(values.length, storage);
        data.put(values);
        return new CFloatDenseMatrix(data, rows, columns, offset, stride, orientation);
    }

    public CFloatDenseMatrix createMatrix(final float[] values, final int rows, final int columns,
            final Orientation orientation) {
        return createMatrix(values, rows, columns, 0, 1, orientation);
    }

    public CFloatDenseMatrix createMatrix(final int rows, final int columns) {
        return new CFloatDenseMatrix(rows, columns, Orientation.DEFAULT, storage);
    }

    public CFloatDenseMatrix createMatrix(final int rows, final int columns, final Orientation orientation) {
        return new CFloatDenseMatrix(rows, columns, orientation, storage);
    }

    public CFloatDenseVector createRowVector(final ComplexFloat... values) {
        return new CFloatDenseVector(Orientation.ROW, storage, values);
    }

    public CFloatDenseVector createVector(final float[] values, final int size, final int offset, final int stride,
            final Orientation orientation) {
        FloatBuffer data = AbstractCFloatDense.createFloatBuffer(values.length, storage);
        data.put(values);
        return new CFloatDenseVector(data, size, offset, stride, orientation);
    }

    public CFloatDenseVector createVector(final int size, final Orientation orientation) {
        return new CFloatDenseVector(size, orientation, storage);
    }
}
