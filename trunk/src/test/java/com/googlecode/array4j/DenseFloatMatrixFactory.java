package com.googlecode.array4j;

public final class DenseFloatMatrixFactory implements FloatMatrixFactory<DenseFloatMatrix, DenseFloatVector> {
    public DenseFloatMatrix createMatrix(final float[] data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        return new DenseFloatMatrix(data, rows, columns, offset, stride, orientation);
    }

    public DenseFloatMatrix createMatrix(final float[] values, final int rows, final int columns,
            final Orientation orientation) {
        return new DenseFloatMatrix(values, rows, columns, orientation);
    }

    public DenseFloatMatrix createMatrix(final int rows, final int columns) {
        return new DenseFloatMatrix(rows, columns);
    }

    public DenseFloatMatrix createMatrix(final int rows, final int columns, final Orientation orientation) {
        return new DenseFloatMatrix(rows, columns, orientation);
    }

    public DenseFloatVector createRowVector(final float... values) {
        return new DenseFloatVector(Orientation.ROW, values);
    }
}
