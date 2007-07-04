package com.googlecode.array4j;

public interface FloatMatrixFactory<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    M createMatrix(float[] data, int rows, int columns, int offset, int stride, Orientation orientation);

    M createMatrix(float[] data, int rows, int columns, Orientation row);

    M createMatrix(int rows, int columns);

    M createMatrix(int rows, int columns, Orientation orientation);
}
