package net.lunglet.array4j.matrix.math;

import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.Matrix;
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
        throw new NotImplementedException();
    }

    /** Scalar subtraction. */
    public static void minusEquals(final FloatMatrix x, final float value) {
        throw new NotImplementedException();
    }

    /** Scalar addition. */
    public static void plusEquals(final FloatMatrix x, final float value) {
        throw new NotImplementedException();
    }

    /** Element-wise addition without broadcasting. */
    public static void plusEquals(final FloatMatrix a, final FloatMatrix b) {
        checkSameDimensions(a, b);
//        if (!(other instanceof FloatDenseMatrix)) {
//            throw new NotImplementedException();
//        }
//        FloatDenseBLAS.DEFAULT.axpy(1.0f, ((FloatDenseMatrix) other).asVector(), asVector());
        throw new NotImplementedException();
    }

    /** Scalar multiplication. */
    public static void timesEquals(final FloatMatrix x, final float value) {
//        FloatDenseBLAS.DEFAULT.scal(value, asVector());
        throw new NotImplementedException();
    }

    private MatrixMath() {
    }
}
