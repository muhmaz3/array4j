package com.googlecode.array4j;

public interface DoubleMatrix<M extends DoubleMatrix<M, V>, V extends DoubleVector<V>> extends Matrix<M, V>,
        DoubleArray<M> {
    void divideEquals(double value);

    double get(int row, int column);

    /**
     * In-place subtraction of a scalar value.
     */
    void minusEquals(double value);

    /**
     * In-place addition of a scalar value.
     */
    void plusEquals(double value);

    void set(int row, int column, double value);

    void setColumn(int column, DoubleVector<?> columnVector);

    void setRow(int row, DoubleVector<?> rowVector);

    /**
     * In-place multiplication with a scalar value.
     */
    void timesEquals(double value);

    double[][] toColumnArrays();

    double[][] toRowArrays();
}
