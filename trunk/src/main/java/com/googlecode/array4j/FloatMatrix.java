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

    FloatMatrix<?, ?> times(FloatMatrix<?, ?> matrix);
}
