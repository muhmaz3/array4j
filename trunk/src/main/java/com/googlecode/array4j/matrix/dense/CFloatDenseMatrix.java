package com.googlecode.array4j.matrix.dense;

import com.googlecode.array4j.matrix.ComplexFloatMatrix;

public interface CFloatDenseMatrix extends ComplexFloatMatrix, DenseMatrix {
    /** {@inheritDoc} */
    Iterable<CFloatDenseVector> columnsIterator();

    /** {@inheritDoc} */
    Iterable<CFloatDenseVector> rowsIterator();

    /** {@inheritDoc} */
    CFloatDenseMatrix transpose();
}
