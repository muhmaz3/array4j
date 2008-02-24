package com.googlecode.array4j.matrix.dense;

import com.googlecode.array4j.matrix.ComplexFloatVector;

public interface CFloatDenseVector extends CFloatDenseMatrix, ComplexFloatVector, DenseVector {
    CFloatDenseVector transpose();
}
