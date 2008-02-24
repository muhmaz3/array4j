package net.lunglet.array4j.matrix.dense;

import net.lunglet.array4j.matrix.FloatVector;

public interface FloatDenseVector extends FloatDenseMatrix, FloatVector, DenseVector {
    /** {@inheritDoc} */
    FloatDenseVector transpose();
}
