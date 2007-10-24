package com.googlecode.array4j;

import java.util.List;

public interface Matrix extends Array {
    /** Get a column vector. */
    Vector column(int column);

    /** Returns the number of columns. */
    int columns();

    /** Returns an iterator over the column vectors. */
    Iterable<? extends Vector> columnsIterator();

    /** Get a list of column vectors. */
    List<? extends Vector> columnsList();

    /**
     * Returns <CODE>true</CODE> if the matrix has the same number of rows and
     * columns.
     */
    boolean isSquare();

    /** Get a row vector. */
    Vector row(int row);

    /** Returns the number of rows. */
    int rows();

    /** Returns an iterator over the row vectors. */
    Iterable<? extends Vector> rowsIterator();

    /** Transpose matrix. */
    Matrix transpose();
}
