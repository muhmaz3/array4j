package net.lunglet.array4j.matrix;

import java.util.List;

/**
 * Single-precision floating point matrix.
 */
public interface FloatMatrix extends Matrix {
    /** {@inheritDoc} */
    FloatVector column(int column);

    /** {@inheritDoc} */
    Iterable<? extends FloatVector> columnsIterator();

    /** {@inheritDoc} */
    List<? extends FloatVector> columnsList();

    /** Get matrix element. */
    float get(int row, int column);

    /** {@inheritDoc} */
    FloatVector row(int row);

    /** {@inheritDoc} */
    Iterable<? extends FloatVector> rowsIterator();

    /** Set matrix element. */
    void set(int row, int column, float value);

    /** Set the values in a column of the matrix. */
    void setColumn(int column, FloatVector columnVector);

    /** Set the values in a row of the matrix. */
    void setRow(int row, FloatVector rowVector);

    /** Pack matrix into an array. */
    float[] toArray();

    /** Return columns as an array of float arrays. */
    float[][] toColumnArrays();

    /** Return rows as an array of float arrays. */
    float[][] toRowArrays();

    /** {@inheritDoc} */
    FloatMatrix transpose();
}
