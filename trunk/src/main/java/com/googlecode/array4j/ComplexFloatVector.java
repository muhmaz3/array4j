package com.googlecode.array4j;

/**
 * Complex single-precision floating point vector.
 */
public interface ComplexFloatVector extends ComplexVector, ComplexFloatMatrix {
    /** Get value of element at index. */
    ComplexFloat get(int index);

    /** Set value of element at index. */
    void set(int index, ComplexFloat value);

    /** {@inheritDoc} */
    ComplexFloatVector transpose();
}
