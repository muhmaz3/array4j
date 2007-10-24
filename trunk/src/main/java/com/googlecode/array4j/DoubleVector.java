package com.googlecode.array4j;

public interface DoubleVector extends DoubleMatrix, Vector {
    double get(int index);

    void set(int index, double value);

    DoubleVector transpose();
}
