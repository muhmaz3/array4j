package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public class DenseVector extends AbstractDenseVector<DenseVector> {
    public DenseVector(final int dimension) {
        super(dimension, 1);
    }

    public DenseVector(final Vector other) {
        super(other);
    }

    private DenseVector(final double[] values) {
        super(values, values.length, 1);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseVector(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }

    /** Constructor for making new vectors with a given shape. */
    public DenseVector(final int[] shape) {
        // TODO should maybe do some checks on shape to ensure that the
        // following call is valid
        super(shape[0], 1);
    }

    public static DenseVector valueOf(final double... values) {
        return new DenseVector(values);
    }
}
