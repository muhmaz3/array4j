package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public class DenseRowVector extends AbstractDenseVector<DenseRowVector>
        implements RowVector<DenseRowVector> {
    public DenseRowVector(final int dimension) {
        // row vector has 1 row and N columns
        super(1, dimension);
    }

    /** Constructor for making new vectors with a given shape. */
    public DenseRowVector(final int[] shape) {
        // TODO should maybe do some checks on shape to ensure that the
        // following call is valid
        super(1, shape[1]);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseRowVector(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }
}
