package com.googlecode.array4j;

public interface FloatMatrix<M extends FloatMatrix<M, V>, V extends FloatVector<V>> extends Matrix<M, V>, FloatArray<M> {
    void setRow(int row, FloatVector<?> rowVector);

    void setColumn(int column, FloatVector<?> columnVector);

    float[][] toRowArrays();

    float[][] toColumnArrays();
}
