package net.lunglet.array4j.matrix;

/**
 * Double-precision floating point matrix.
 */
public interface DoubleMatrix extends Matrix {
    /** {@inheritDoc} */
    DoubleVector column(int column);

    /** Get matrix element. */
    double get(int row, int column);

    /** {@inheritDoc} */
    DoubleVector row(int row);

    /** Set matrix element. */
    void set(int row, int column, double value);

    /** Set the values in a column of the matrix. */
    void setColumn(int column, DoubleVector columnVector);

    /** Set the values in a row of the matrix. */
    void setRow(int row, DoubleVector rowVector);

    /** Return columns as an array of double arrays. */
    double[][] toColumnArrays();

    /** Return rows as an array of double arrays. */
    double[][] toRowArrays();

    /** {@inheritDoc} */
    DoubleMatrix transpose();
}
