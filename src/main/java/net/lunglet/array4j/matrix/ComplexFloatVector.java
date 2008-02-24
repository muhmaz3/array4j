package net.lunglet.array4j.matrix;

import net.lunglet.array4j.ComplexFloat;

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
