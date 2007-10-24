package com.googlecode.array4j;

/**
 * Complex matrix.
 */
public interface ComplexMatrix extends Matrix {
    /** Conjugate matrix in-place. */
    void conj();

    /** {@inheritDoc} */
    ComplexMatrix transpose();
}
