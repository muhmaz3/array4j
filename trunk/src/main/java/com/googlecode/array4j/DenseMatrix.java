package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public class DenseMatrix extends AbstractDenseMatrix<DenseMatrix> {
    public DenseMatrix(final int rows, final int columns) {
        super(rows, columns);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseMatrix(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }

    /** Constructor for making new vectors with a given shape. */
    public DenseMatrix(final int[] shape) {
        // TODO should check that ndim is only 2
        super(shape[0], shape[1]);
    }

    private DenseMatrix(final double[][] values) {
        super(values);
    }

    public static DenseMatrix valueOf(final double[][] values) {
        return new DenseMatrix(values);
    }
}
