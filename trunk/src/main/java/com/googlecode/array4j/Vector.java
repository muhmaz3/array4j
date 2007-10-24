package com.googlecode.array4j;

public interface Vector extends Matrix {
    boolean isColumnVector();

    boolean isRowVector();

    int length();

    Vector transpose();
}
