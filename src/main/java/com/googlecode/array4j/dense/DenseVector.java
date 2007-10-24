package com.googlecode.array4j.dense;

import com.googlecode.array4j.Vector;

public interface DenseVector extends DenseMatrix, Vector {
    DenseVector transpose();
}
