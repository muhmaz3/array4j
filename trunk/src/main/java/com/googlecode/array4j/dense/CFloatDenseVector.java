package com.googlecode.array4j.dense;

import com.googlecode.array4j.ComplexFloatVector;

public interface CFloatDenseVector extends CFloatDenseMatrix, ComplexFloatVector, DenseVector {
    CFloatDenseVector transpose();
}
