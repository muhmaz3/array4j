package com.googlecode.array4j;

public interface DoubleMatrix extends Matrix, DoubleArray {
    DoubleVector column(int column);

    double get(int row, int column);

    DoubleVector row(int row);

    void set(int row, int column, double value);

    void setColumn(int column, DoubleVector columnVector);

    void setRow(int row, DoubleVector rowVector);

    double[][] toColumnArrays();

    double[][] toRowArrays();

    /** {@inheritDoc} */
    DoubleMatrix transpose();
}
