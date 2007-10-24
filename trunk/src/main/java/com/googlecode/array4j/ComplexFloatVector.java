package com.googlecode.array4j;

public interface ComplexFloatVector extends ComplexFloatMatrix, ComplexVector {
    ComplexFloat get(int index);

    void set(int index, ComplexFloat value);

    ComplexFloatVector transpose();
}
