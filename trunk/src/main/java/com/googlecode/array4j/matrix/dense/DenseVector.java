package com.googlecode.array4j.matrix.dense;

import com.googlecode.array4j.matrix.Vector;

public interface DenseVector extends DenseMatrix, Vector {
    /** {@inheritDoc} */
    DenseVector transpose();
}
