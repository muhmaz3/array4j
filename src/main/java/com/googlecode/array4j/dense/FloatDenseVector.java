package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatVector;

public interface FloatDenseVector extends FloatDenseMatrix, FloatVector, DenseVector {
    FloatDenseVector transpose();
}
