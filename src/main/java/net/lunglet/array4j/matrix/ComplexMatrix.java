package net.lunglet.array4j.matrix;

/**
 * Complex matrix.
 */
public interface ComplexMatrix extends Matrix {
    /** Conjugate matrix in-place. */
    void conjEquals();

    /** {@inheritDoc} */
    ComplexMatrix transpose();
}
