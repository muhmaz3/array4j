package com.googlecode.array4j;

public interface ComplexFloatMatrixFactory<M extends ComplexFloatMatrix, V extends ComplexFloatVector> {
    M createMatrix(float[] values, int rows, int columns, int offset, int stride, Order orientation);

    M createMatrix(float[] values, int rows, int columns, Order row);

    M createMatrix(int rows, int columns);

    M createMatrix(int rows, int columns, Order orientation);

    V createRowVector(ComplexFloat... values);

    V createVector(float[] data, int size, int offset, int stride, Order orientation);

    V createVector(int size, Order orientation);
}
