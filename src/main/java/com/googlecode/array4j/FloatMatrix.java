package com.googlecode.array4j;

public interface FloatMatrix<M extends FloatMatrix, V extends FloatVector> extends Matrix<M, V>, FloatArray<M> {
    void setRow(int row, FloatVector rowVector);

    void setColumn(int column, FloatVector columnVector);

    float[][] toRowArrays();

    float[][] toColumnArrays();
}
