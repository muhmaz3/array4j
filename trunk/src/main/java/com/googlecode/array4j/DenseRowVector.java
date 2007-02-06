package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public final class DenseRowVector extends AbstractDenseVector<DenseRowVector>
        implements RowVector<DenseRowVector> {
    public DenseRowVector(final int dimension) {
        super(dimension, 1);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseRowVector(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }
}
