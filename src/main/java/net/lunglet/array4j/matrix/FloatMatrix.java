package net.lunglet.array4j.matrix;

import java.util.List;

/**
 * Single-precision floating point matrix.
 */
public interface FloatMatrix extends Matrix {
    /** {@inheritDoc} */
    FloatVector asVector();

    /** {@inheritDoc} */
    FloatVector column(int column);

    /** {@inheritDoc} */
    Iterable<? extends FloatVector> columnsIterator();

    /** {@inheritDoc} */
    List<? extends FloatVector> columnsList();

    /** Scalar division. */
    void divideEquals(float value);

    float get(int row, int column);

    /** Scalar subtraction. */
    void minusEquals(float value);

    /** Scalar addition. */
    void plusEquals(float value);

    /** Element-wise addition without broadcasting. */
    void plusEquals(FloatMatrix other);

    /** {@inheritDoc} */
    FloatVector row(int row);

    /** {@inheritDoc} */
    Iterable<? extends FloatVector> rowsIterator();

    void set(int row, int column, float value);

    /** Set the values in a column of the matrix. */
    void setColumn(int column, FloatVector columnVector);

    /** Set the values in a row of the matrix. */
    void setRow(int row, FloatVector rowVector);

    /** Scalar multiplication. */
    void timesEquals(float value);

    /** Pack matrix into an array. */
    float[] toArray();

    /** Convert columns to an array of arrays. */
    float[][] toColumnArrays();

    /** Convert rows to an array of arrays. */
    float[][] toRowArrays();

    /** {@inheritDoc} */
    FloatMatrix transpose();
}
