package com.googlecode.array4j;

public interface FloatVector extends Vector, FloatMatrix {
    void divideEquals(float value);

    float get(int index);

    FloatVector minus(FloatVector other);

    void minusEquals(float value);

    void plusEquals(float value);

    void plusEquals(FloatVector other);

    void set(int index, float value);

    void timesEquals(float value);

    /** {@inheritDoc} */
    FloatVector transpose();
}
