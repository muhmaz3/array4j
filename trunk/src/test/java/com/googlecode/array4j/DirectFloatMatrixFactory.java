package com.googlecode.array4j;

public final class DirectFloatMatrixFactory implements FloatMatrixFactory<DirectFloatMatrix, DirectFloatVector> {
    public DirectFloatMatrix createMatrix(final float[] data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        return new DirectFloatMatrix(data, rows, columns, offset, stride, orientation);
    }

    public DirectFloatMatrix createMatrix(final float[] values, final int rows, final int columns,
            final Orientation orientation) {
        return new DirectFloatMatrix(values, rows, columns, orientation);
    }

    public DirectFloatMatrix createMatrix(final int rows, final int columns) {
        return new DirectFloatMatrix(rows, columns);
    }

    public DirectFloatMatrix createMatrix(final int rows, final int columns, final Orientation orientation) {
        return new DirectFloatMatrix(rows, columns, orientation);
    }

    public DirectFloatVector createRowVector(final float... values) {
        return new DirectFloatVector(Orientation.ROW, values);
    }
}
