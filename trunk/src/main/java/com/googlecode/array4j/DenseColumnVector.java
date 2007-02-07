package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public class DenseColumnVector extends AbstractDenseVector<DenseColumnVector>
        implements ColumnVector<DenseColumnVector> {
    public DenseColumnVector(final int dimension) {
        super(1, dimension);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseColumnVector(final DoubleBuffer buffer, final int[] shape) {
        super(buffer, shape);
    }
}
