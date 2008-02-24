package com.googlecode.array4j.matrix.dense;

import com.googlecode.array4j.matrix.FloatVector;

public interface FloatDenseVector extends FloatDenseMatrix, FloatVector, DenseVector {
    /** {@inheritDoc} */
    FloatDenseVector transpose();
}
