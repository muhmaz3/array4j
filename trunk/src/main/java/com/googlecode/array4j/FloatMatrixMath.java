package com.googlecode.array4j;

import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.packed.FloatPackedMatrix;

// TODO move this class to com.googlecode.array4j.math

public final class FloatMatrixMath {
    public static float dot(final FloatVector<?> x, final FloatVector<?> value) {
        return 0.0f;
    }

    public static void logEquals(final FloatMatrix<?, ?> matrix) {
        // TODO special cases for certain matrix types
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, (float) Math.log(matrix.get(i, j)));
            }
        }
    }

    public static FloatDenseMatrix times(final FloatMatrix<?, ?> x, final FloatMatrix<?, ?> y) {
        return null;
    }

    public static FloatPackedMatrix timesTranspose(final FloatDenseMatrix a) {
        return null;
    }

    private FloatMatrixMath() {
    }
}
