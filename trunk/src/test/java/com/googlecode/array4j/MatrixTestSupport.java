package com.googlecode.array4j;

public final class MatrixTestSupport {
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
