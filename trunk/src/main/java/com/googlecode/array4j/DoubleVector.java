package com.googlecode.array4j;

public interface DoubleVector extends Vector, DoubleMatrix {
    double get(int index);

    void set(int index, double value);

    /** {@inheritDoc} */
    DoubleVector transpose();
}
