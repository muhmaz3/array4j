package com.googlecode.array4j;

import java.nio.FloatBuffer;

public final class DirectFloatMatrixFactory implements FloatMatrixFactory<DirectFloatMatrix, DirectFloatVector> {
    public DirectFloatMatrix createMatrix(final float[] data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        return new DirectFloatMatrix(data, rows, columns, offset, stride, orientation);
    }

    public DirectFloatMatrix createMatrix(final float[] values, final int rows, final int columns,
            final Orientation orientation) {
        // TODO create matrix with random backing store setup that has the
        // required number of rows and columns
        return new DirectFloatMatrix(values, rows, columns, orientation);
    }

    public DirectFloatMatrix createMatrix(final int rows, final int columns) {
        // TODO create matrix with random backing store setup that has the
        // required number of rows and columns
        return new DirectFloatMatrix(rows, columns);
    }

    public DirectFloatMatrix createMatrix(final int rows, final int columns, final Orientation orientation) {
        // TODO create matrix with random backing store setup that has the
        // required number of rows and columns
        return new DirectFloatMatrix(rows, columns, orientation);
    }

    public DirectFloatVector createRowVector(final float... values) {
        return new DirectFloatVector(Orientation.ROW, values);
    }

    public DirectFloatVector createVector(final float[] values, final int size, final int offset, final int stride,
            final Orientation orientation) {
        FloatBuffer data = DirectFloatMatrix.createBuffer(values);
        return new DirectFloatVector(data, size, offset, stride, orientation);
    }

    public DirectFloatVector createVector(final int size, final Orientation orientation) {
        // TODO create vector with random backing store setup that has the
        // required number of rows and columns
        return new DirectFloatVector(size, orientation);
    }
}
