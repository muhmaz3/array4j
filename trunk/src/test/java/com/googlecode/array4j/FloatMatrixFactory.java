package com.googlecode.array4j;

public interface FloatMatrixFactory {
    FloatMatrix createMatrix(float[] values, int rows, int columns, int offset, int stride, Order order);

    FloatMatrix createMatrix(float[] values, int rows, int columns, Order order);

    FloatMatrix createMatrix(int rows, int columns);

    FloatMatrix createMatrix(int rows, int columns, Order order);

    FloatVector createRowVector(float... values);

    FloatVector createVector(float[] data, int size, int offset, int stride, Direction direction);

    FloatVector createVector(int size, Direction direction);
}
