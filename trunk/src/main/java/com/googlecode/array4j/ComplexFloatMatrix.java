package com.googlecode.array4j;

/**
 * Complex single precision floating point matrix.
 */
public interface ComplexFloatMatrix extends ComplexMatrix, ComplexFloatArray {
    Iterable<? extends ComplexFloatVector> columnsIterator();

    ComplexFloat get(int row, int column);

    Iterable<? extends ComplexFloatVector> rowsIterator();

    void set(int row, int column, ComplexFloat value);

    void setColumn(int column, ComplexFloatVector columnVector);

    void setRow(int row, ComplexFloatVector rowVector);

    ComplexFloat[][] toColumnArrays();

    ComplexFloat[][] toRowArrays();

    ComplexFloatMatrix transpose();
}
