package com.googlecode.array4j;

import com.googlecode.array4j.blas.FloatDenseBLAS;
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
        if (!(x instanceof FloatDenseMatrix)) {
            throw new IllegalArgumentException();
        }
        if (!(y instanceof FloatDenseMatrix)) {
            throw new IllegalArgumentException();
        }
        FloatDenseMatrix a = (FloatDenseMatrix) x;
        FloatDenseMatrix b = (FloatDenseMatrix) y;
        FloatDenseMatrix c = new FloatDenseMatrix(a.rows(), b.columns(), Orientation.COLUMN, Storage.DIRECT);
        FloatDenseBLAS.DEFAULT.gemm(1.0f, a, b, 0.0f, c);
        return c;
    }

    public static FloatPackedMatrix timesTranspose(final FloatDenseMatrix a) {
        FloatDenseMatrix c = new FloatDenseMatrix(a.rows(), a.rows(), a.orientation(), a.storage());
        FloatDenseBLAS.DEFAULT.syrk(1.0f, a, 0.0f, c);
        return FloatPackedMatrix.valueOf(c);
    }

    private FloatMatrixMath() {
    }
}
