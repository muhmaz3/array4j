package com.googlecode.array4j;

public interface FloatMatrixFactory<M extends FloatMatrix, V extends FloatVector> {
    M createMatrix(float[] values, int rows, int columns, int offset, int stride, Order orientation);

    M createMatrix(float[] values, int rows, int columns, Order row);

    M createMatrix(int rows, int columns);

    M createMatrix(int rows, int columns, Order orientation);

    V createRowVector(float... values);

    V createVector(float[] data, int size, int offset, int stride, Order orientation);

    V createVector(int size, Order orientation);
}
