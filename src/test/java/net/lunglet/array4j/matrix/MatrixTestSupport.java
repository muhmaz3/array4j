package net.lunglet.array4j.matrix;

import static org.junit.Assert.assertEquals;

public final class MatrixTestSupport {
    public static void checkMatrix(final FloatMatrix expected, final FloatMatrix actual, final float eps) {
        checkMatrix(expected, actual, eps, false);
    }

    public static void checkMatrix(final FloatMatrix expected, final FloatMatrix actual, final float eps,
            final boolean relative) {
        assertEquals(expected.rows(), actual.rows());
        assertEquals(expected.columns(), actual.columns());
        for (int i = 0; i < actual.rows(); i++) {
            for (int j = 0; j < actual.columns(); j++) {
                float expectedij = expected.get(i, j);
                float epsij = relative ? expectedij * eps : eps;
                assertEquals(expectedij, actual.get(i, j), epsij);
            }
        }
    }

    public static void populateMatrix(final FloatMatrix x) {
        for (int i = 0, k = 1; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++, k++) {
                x.set(i, j, k);
            }
        }
    }

    private MatrixTestSupport() {
    }
}
