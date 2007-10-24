package com.googlecode.array4j.dense;

import com.googlecode.array4j.ComplexFloatMatrix;

public interface CFloatDenseMatrix extends ComplexFloatMatrix, DenseMatrix {
    CFloatDenseMatrix transpose();

    Iterable<CFloatDenseVector> columnsIterator();

    Iterable<CFloatDenseVector> rowsIterator();
}
