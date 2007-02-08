package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public class DenseColumnVector extends AbstractDenseVector<DenseColumnVector>
        implements ColumnVector<DenseColumnVector> {
    public DenseColumnVector(final int dimension) {
        // column vector has N rows and 1 column
        super(dimension, 1);
    }

    /** Constructor for making new vectors with a given shape. */
    public DenseColumnVector(final int[] shape) {
        // TODO should maybe do some checks on shape to ensure that the
        // following call is valid
        super(shape[0], 1);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseColumnVector(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }
}
