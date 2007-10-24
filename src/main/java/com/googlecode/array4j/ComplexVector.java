package com.googlecode.array4j;

/**
 * Complex vector.
 */
public interface ComplexVector extends Vector, ComplexMatrix {
    /** {@inheritDoc} */
    ComplexVector transpose();
}
