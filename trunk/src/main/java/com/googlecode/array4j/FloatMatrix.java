package com.googlecode.array4j;

public interface FloatMatrix<M extends FloatMatrix<M, V>, V extends FloatVector<V>> extends Matrix<M, V>, FloatArray<M> {
    V createColumnVector(float... values);

    V createRowVector(float... values);

    void setColumn(int column, FloatVector<?> columnVector);

    void setRow(int row, FloatVector<?> rowVector);

    float[][] toColumnArrays();

    float[][] toRowArrays();
}
