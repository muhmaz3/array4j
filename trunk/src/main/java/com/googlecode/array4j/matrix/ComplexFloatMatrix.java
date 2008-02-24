package com.googlecode.array4j.matrix;

import com.googlecode.array4j.ComplexFloat;

/**
 * Complex single-precision floating point matrix.
 */
public interface ComplexFloatMatrix extends ComplexMatrix {
    /** {@inheritDoc} */
    Iterable<? extends ComplexFloatVector> columnsIterator();

    ComplexFloat get(int row, int column);

    /** {@inheritDoc} */
    Iterable<? extends ComplexFloatVector> rowsIterator();

    void set(int row, int column, ComplexFloat value);

    void setColumn(int column, ComplexFloatVector columnVector);

    void setRow(int row, ComplexFloatVector rowVector);

    ComplexFloat[][] toColumnArrays();

    ComplexFloat[][] toRowArrays();

    /** {@inheritDoc} */
    ComplexFloatMatrix transpose();
}
