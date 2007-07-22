package com.googlecode.array4j;

public interface ComplexFloatMatrixFactory<M extends ComplexFloatMatrix<M, V>, V extends ComplexFloatVector<V>> {
    M createMatrix(float[] values, int rows, int columns, int offset, int stride, Orientation orientation);

    M createMatrix(float[] values, int rows, int columns, Orientation row);

    M createMatrix(int rows, int columns);

    M createMatrix(int rows, int columns, Orientation orientation);

    V createRowVector(ComplexFloat... values);

    V createVector(float[] data, int size, int offset, int stride, Orientation orientation);

    V createVector(int size, Orientation orientation);
}
