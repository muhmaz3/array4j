package com.googlecode.array4j;

public final class FloatUtils {
    public static FloatVector<?> createColumnVector(final int size) {
        return new DenseFloatVector(size, Orientation.COLUMN);
    }

    public static FloatMatrix<?, ?> createMatrix(final double[]... values) {
        return createMatrix(Orientation.ROW, values);
    }

    public static FloatMatrix<?, ?> createMatrix(final Orientation orientation, final double[]... values) {
        return null;
    }

    public static FloatVector<?> createRowVector(final float... values) {
        return null;
    }

    public static FloatVector<?> createRowVector(final int size) {
        return new DenseFloatVector(size, Orientation.ROW);
    }

    private FloatUtils() {
    }
}
