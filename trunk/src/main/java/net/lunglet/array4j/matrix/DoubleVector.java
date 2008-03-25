package net.lunglet.array4j.matrix;

/**
 * Double-precision floating point vector.
 */
public interface DoubleVector extends Vector, DoubleMatrix {
    /** Get value of element at index. */
    double get(int index);

    /** Set value of element at index. */
    void set(int index, double value);

    /** {@inheritDoc} */
    DoubleVector transpose();
}
