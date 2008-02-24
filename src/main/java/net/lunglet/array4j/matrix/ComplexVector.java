package net.lunglet.array4j.matrix;


/**
 * Complex vector.
 */
public interface ComplexVector extends Vector, ComplexMatrix {
    /** {@inheritDoc} */
    ComplexVector transpose();
}
