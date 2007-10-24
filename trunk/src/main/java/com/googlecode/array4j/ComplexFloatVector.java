package com.googlecode.array4j;

/**
 * Complex single precision floating point vector.
 */
public interface ComplexFloatVector extends ComplexVector, ComplexFloatMatrix {
    ComplexFloat get(int index);

    void set(int index, ComplexFloat value);

    /** {@inheritDoc} */
    ComplexFloatVector transpose();
}
