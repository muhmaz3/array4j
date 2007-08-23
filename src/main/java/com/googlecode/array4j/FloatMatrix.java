package com.googlecode.array4j;

public interface FloatMatrix<M extends FloatMatrix<M, V>, V extends FloatVector<V>> extends Matrix<M, V>, FloatArray<M> {
    /**
     * In-place division by a scalar value.
     */
    void divideEquals(float value);

    float get(int row, int column);

    /**
     * In-place subtraction of a scalar value.
     */
    void minusEquals(float value);

    /**
     * In-place addition of a scalar value.
     */
    void plusEquals(float value);

    void set(int row, int column, float value);

    void setColumn(int column, FloatVector<?> columnVector);

    void setRow(int row, FloatVector<?> rowVector);

    /**
     * In-place multiplication with a scalar value.
     */
    void timesEquals(float value);

    float[][] toColumnArrays();

    float[][] toRowArrays();
}
