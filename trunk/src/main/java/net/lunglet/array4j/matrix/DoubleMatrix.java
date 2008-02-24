package net.lunglet.array4j.matrix;

public interface DoubleMatrix extends Matrix {
    /** {@inheritDoc} */
    DoubleVector column(int column);

    double get(int row, int column);

    /** {@inheritDoc} */
    DoubleVector row(int row);

    void set(int row, int column, double value);

    void setColumn(int column, DoubleVector columnVector);

    void setRow(int row, DoubleVector rowVector);

    double[][] toColumnArrays();

    double[][] toRowArrays();

    /** {@inheritDoc} */
    DoubleMatrix transpose();
}
