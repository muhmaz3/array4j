package net.lunglet.array4j.math;

import net.lunglet.array4j.Order;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.blas.FloatDenseBLAS;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.DenseMatrix;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;
import net.lunglet.array4j.matrix.packed.FloatPackedMatrix;
import net.lunglet.array4j.matrix.packed.PackedFactory;
import org.apache.commons.lang.NotImplementedException;

public final class FloatMatrixMath {
    public static FloatVector minus(final FloatVector x, final FloatVector y) {
        if (!(x instanceof FloatDenseVector)) {
            throw new NotImplementedException();
        }
        if (!(y instanceof FloatDenseVector)) {
            throw new NotImplementedException();
        }
        FloatDenseVector z = DenseFactory.copyOf(x);
        FloatDenseBLAS.DEFAULT.axpy(-1.0f, (FloatDenseVector) y, z);
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
            FloatDenseMatrix c = DenseFactory.floatMatrix(a.rows(), b.columns(), Order.COLUMN, Storage.DIRECT);
            // TODO can handle non-unit strides here by using gemv or dot
            FloatDenseBLAS.DEFAULT.gemm(1.0f, a, b, 0.0f, c);
            return c;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static FloatPackedMatrix timesTranspose(final FloatDenseMatrix a) {
        FloatDenseMatrix c = DenseFactory.floatMatrix(a.rows(), a.rows(), a.order(), a.storage());
        FloatDenseBLAS.DEFAULT.syrk(1.0f, a, 0.0f, c);
        return PackedFactory.symmetric(c);
    }

    private FloatMatrixMath() {
    }
}
