package com.googlecode.array4j;

// TODO move this class to com.googlecode.array4j.math

public final class FloatMatrixMath {
    private FloatMatrixMath() {
    }

    public static void logEquals(final FloatMatrix<?, ?> matrix) {
        // TODO special cases for certain matrix types
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, (float) Math.log(matrix.get(i, j)));
            }
        }
    }
}
