package com.googlecode.array4j;

public interface ComplexFloatMatrixFactory {
    ComplexFloatMatrix createMatrix(float[] values, int rows, int columns, int offset, int stride, Order order);

    ComplexFloatMatrix createMatrix(float[] values, int rows, int columns, Order row);

    ComplexFloatMatrix createMatrix(int rows, int columns);

    ComplexFloatMatrix createMatrix(int rows, int columns, Order order);

    ComplexFloatVector createRowVector(ComplexFloat... values);

    ComplexFloatVector createVector(float[] data, int size, int offset, int stride, Order orientation);

    ComplexFloatVector createVector(int size, Order orientation);
}
