package com.googlecode.array4j;

import java.util.Random;

public interface FloatMatrix<M extends FloatMatrix<M, V>, V extends FloatVector<V>> extends Matrix<M, V>, FloatArray<M> {
    // TODO make this method a static somewhere
    void fill(float value);

    // TODO make this method a static somewhere
    void fillRandom(Random rng);

    float get(int row, int column);

    void set(int row, int column, float value);

    void setColumn(int column, FloatVector<?> columnVector);

    void setRow(int row, FloatVector<?> rowVector);

    float[][] toColumnArrays();

    float[][] toRowArrays();

    /**
     * Matrix multiplication.
     */
    // TODO this should probably be a static method somewhere instead of being
    // part of this interface, so that it is closer to how BLAS is organised
    FloatMatrix<?, ?> times(FloatMatrix<?, ?> matrix);

    /**
     * In-place addition of a scalar value.
     */
    void plusEquals(float value);

    /**
     * In-place subtraction of a scalar value.
     */
    void minusEquals(float value);

    /**
     * In-place multiplication with a scalar value.
     */
    void timesEquals(float value);

    /**
     * In-place division by a scalar value.
     */
    void divideEquals(float value);
}
