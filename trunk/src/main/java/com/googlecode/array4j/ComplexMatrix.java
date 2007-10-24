package com.googlecode.array4j;

public interface ComplexMatrix extends Matrix {
    void conj();

    ComplexMatrix transpose();
}
