package net.lunglet.array4j.matrix.dense;

import net.lunglet.array4j.matrix.ComplexFloatVector;

public interface CFloatDenseVector extends CFloatDenseMatrix, ComplexFloatVector, DenseVector {
    CFloatDenseVector transpose();
}
