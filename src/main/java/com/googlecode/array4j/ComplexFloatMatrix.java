package com.googlecode.array4j;

public interface ComplexFloatMatrix<M extends ComplexFloatMatrix<M, V>, V extends ComplexFloatVector<V>> extends
        Matrix<M, V>, ComplexFloatArray<M> {
    ComplexFloat get(int row, int column);

    void set(int row, int column, ComplexFloat value);

    void setColumn(int column, ComplexFloatVector<?> columnVector);

    void setRow(int row, ComplexFloatVector<?> rowVector);

    ComplexFloat[][] toColumnArrays();

    ComplexFloat[][] toRowArrays();
}
