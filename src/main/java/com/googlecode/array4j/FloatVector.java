package com.googlecode.array4j;

public interface FloatVector extends FloatMatrix, Vector {
    float get(int index);

    void set(int index, float value);

    FloatVector transpose();
}
