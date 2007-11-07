package com.googlecode.array4j.dense;

import com.googlecode.array4j.ComplexFloatMatrix;

public interface CFloatDenseMatrix extends ComplexFloatMatrix, DenseMatrix {
    /** {@inheritDoc} */
    Iterable<CFloatDenseVector> columnsIterator();

    /** {@inheritDoc} */
    Iterable<CFloatDenseVector> rowsIterator();

    /** {@inheritDoc} */
    CFloatDenseMatrix transpose();
}
