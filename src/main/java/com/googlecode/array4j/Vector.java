package com.googlecode.array4j;

public interface Vector extends Matrix {
    Direction direction();

    boolean isColumnVector();

    boolean isRowVector();

    int length();

    /** Change direction of vector. */
    Vector transpose();
}
