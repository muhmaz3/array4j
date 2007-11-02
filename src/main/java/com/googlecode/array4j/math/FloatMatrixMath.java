package com.googlecode.array4j.math;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.blas.FloatDenseBLAS;
import com.googlecode.array4j.dense.DenseFactory;
import com.googlecode.array4j.dense.DenseMatrix;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import com.googlecode.array4j.packed.FloatPackedMatrix;

public final class FloatMatrixMath {
    public static FloatVector minus(final FloatVector x, final FloatVector y) {
        if (!(x instanceof FloatDenseVector)) {
            throw new UnsupportedOperationException();
        }
        if (!(y instanceof FloatDenseVector)) {
            throw new UnsupportedOperationException();
        }
        FloatDenseVector z = DenseFactory.copyOf(x);
        FloatDenseBLAS.DEFAULT.axpy(1.0f, (FloatDenseVector) y, z);
        return z;
    }

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
//        if (!Arrays.equals(x.shape(), y.shape())) {
//            throw new IllegalArgumentException();
//        }
        for (int i = 0; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++) {
                x.set(i, j, x.get(i, j) + y.get(i, j));
            }
        }
    }

    public static FloatDenseMatrix times(final FloatMatrix x, final FloatMatrix y) {
        if (x instanceof DenseMatrix && y instanceof DenseMatrix) {
            FloatDenseMatrix a = (FloatDenseMatrix) x;
            FloatDenseMatrix b = (FloatDenseMatrix) y;
            FloatDenseMatrix c = DenseFactory.createFloatMatrix(a.rows(), b.columns(), Order.COLUMN, Storage.DIRECT);
            // TODO can handle non-unit strides here by using gemv or dot
            FloatDenseBLAS.DEFAULT.gemm(1.0f, a, b, 0.0f, c);
            return c;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static FloatPackedMatrix timesTranspose(final FloatDenseMatrix a) {
        FloatDenseMatrix c = DenseFactory.createFloatMatrix(a.rows(), a.rows(), a.order(), a.storage());
        FloatDenseBLAS.DEFAULT.syrk(1.0f, a, 0.0f, c);
//        return FloatPackedMatrix.valueOf(c);
        return null;
    }

    private FloatMatrixMath() {
    }
}
