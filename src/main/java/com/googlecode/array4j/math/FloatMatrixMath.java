package com.googlecode.array4j.math;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.blas.FloatDenseBLAS;
import com.googlecode.array4j.dense.DenseMatrix;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import com.googlecode.array4j.packed.FloatPackedMatrix;
import java.util.Arrays;

public final class FloatMatrixMath {
    public static float dot(final FloatVector x, final FloatVector y) {
        if (!(x instanceof FloatDenseVector)) {
            throw new IllegalArgumentException();
        }
        if (!(y instanceof FloatDenseVector)) {
            throw new IllegalArgumentException();
        }
        return FloatDenseBLAS.DEFAULT.dot((FloatDenseVector) x, (FloatDenseVector) y);
    }

    public static void logEquals(final FloatMatrix matrix) {
        // TODO special cases for certain matrix types
        for (int i = 0; i < matrix.rows(); i++) {
            for (int j = 0; j < matrix.columns(); j++) {
                matrix.set(i, j, (float) Math.log(matrix.get(i, j)));
            }
        }
    }

    public static void plusEquals(final FloatMatrix x, final FloatMatrix y) {
        // TODO optimize this function by using BLAS when possible
        if (!Arrays.equals(x.shape(), y.shape())) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++) {
                x.set(i, j, x.get(i, j) + y.get(i, j));
            }
        }
    }

    public static FloatDenseMatrix times(final FloatMatrix x, final FloatMatrix y) {
        if (x instanceof DenseMatrix && y instanceof DenseMatrix) {
            final FloatDenseMatrix a;
            final FloatDenseMatrix b;
//            if (x instanceof Vector) {
//                a = ((FloatDenseVector) x).asMatrix();
//            } else {
//                a = (FloatDenseMatrix) x;
//            }
//            if (y instanceof Vector) {
//                b = ((FloatDenseVector) y).asMatrix();
//            } else {
//                b = (FloatDenseMatrix) y;
//            }
//            FloatDenseMatrix c = new FloatDenseMatrix(a.rows(), b.columns(), Order.COLUMN, Storage.DIRECT);
            a = null;
            b = null;
            FloatDenseMatrix c = null;
            // TODO can handle non-unit strides here by using gemv or dot
            FloatDenseBLAS.DEFAULT.gemm(1.0f, a, b, 0.0f, c);
            return c;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static FloatPackedMatrix timesTranspose(final FloatDenseMatrix a) {
//        FloatDenseMatrix c = new FloatDenseMatrix(a.rows(), a.rows(), a.order(), a.storage());
        FloatDenseMatrix c = null;
        FloatDenseBLAS.DEFAULT.syrk(1.0f, a, 0.0f, c);
//        return FloatPackedMatrix.valueOf(c);
        return null;
    }

    private FloatMatrixMath() {
    }
}
