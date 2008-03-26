package net.lunglet.array4j.matrix.math;

import net.lunglet.array4j.blas.FloatDenseBLAS;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.Matrix;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import org.apache.commons.lang.NotImplementedException;

// TODO split into separate classes like FloatMatrixMath later

public final class MatrixMath {
    private static void checkSameDimensions(final Matrix a, final Matrix b) {
        if (a.rows() != b.rows() || a.columns() != b.columns()) {
            throw new IllegalArgumentException();
        }
    }

    /** Scalar division. */
    public static void divideEquals(final FloatMatrix x, final float value) {
        timesEquals(x, 1.0f / value);
    }

    /** Scalar subtraction. */
    public static void minusEquals(final FloatMatrix x, final float value) {
        plusEquals(x, -value);
    }

    /** Scalar addition. */
    public static void plusEquals(final FloatMatrix x, final float value) {
        throw new NotImplementedException();
    }

    /**
     * Element-wise addition without broadcasting.
     * <p>
     * <CODE>a += b</CODE>
     */
    public static void plusEquals(final FloatMatrix a, final FloatMatrix b) {
        checkSameDimensions(a, b);
        if (!(a instanceof FloatDenseMatrix) || !(b instanceof FloatDenseMatrix)) {
            throw new NotImplementedException();
        }
        FloatDenseBLAS.DEFAULT.axpy(1.0f, (FloatDenseMatrix) b, (FloatDenseMatrix) a);
    }

    /** Scalar multiplication. */
    public static void timesEquals(final FloatMatrix x, final float value) {
        if (!(x instanceof FloatDenseMatrix)) {
            throw new NotImplementedException();
        }
        FloatDenseBLAS.DEFAULT.scal(value, (FloatDenseMatrix) x);
    }

    private MatrixMath() {
    }
}
