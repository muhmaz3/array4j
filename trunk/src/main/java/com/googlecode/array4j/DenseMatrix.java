package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public final class DenseMatrix extends AbstractDenseMatrix<DenseMatrix> {
    public DenseMatrix(final int rows, final int columns) {
        super(rows, columns);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseMatrix(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }
}
