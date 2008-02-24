package net.lunglet.array4j.matrix.dense;

import net.lunglet.array4j.matrix.ComplexFloatMatrix;

public interface CFloatDenseMatrix extends ComplexFloatMatrix, DenseMatrix {
    /** {@inheritDoc} */
    Iterable<CFloatDenseVector> columnsIterator();

    /** {@inheritDoc} */
    Iterable<CFloatDenseVector> rowsIterator();

    /** {@inheritDoc} */
    CFloatDenseMatrix transpose();
}
