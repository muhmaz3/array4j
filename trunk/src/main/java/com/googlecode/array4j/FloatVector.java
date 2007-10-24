package com.googlecode.array4j;

public interface FloatVector extends Vector, FloatMatrix {
    /** Get value of element at index. */
    float get(int index);

    /** Set value of element at index. */
    void set(int index, float value);

    /** {@inheritDoc} */
    FloatVector transpose();
}
