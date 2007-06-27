package com.googlecode.array4j;

public interface ComplexFloatMatrix<M extends ComplexFloatMatrix, V extends ComplexFloatVector>
        extends Matrix<M, V>, ObjectArray<ComplexFloat, M> {
    void setColumn(int column, ComplexFloatVector columnVector);

    void setRow(int row, ComplexFloatVector rowVector);

    ComplexFloat[][] toColumnArrays();

    ComplexFloat[][] toRowArrays();
}
