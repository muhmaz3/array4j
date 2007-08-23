package com.googlecode.array4j;

public interface Matrix<M extends Matrix<M, V>, V extends Vector<V>> extends Array<M> {
    V asVector();

    V column(int column);

    int columns();

    Iterable<V> columnsIterator();

    V createColumnVector();

    V createRowVector();

    V row(int row);

    int rows();

    Iterable<V> rowsIterator();

    /**
     * Get a submatrix spanning the column range [column0, column1).
     */
    Matrix<M, V> subMatrixColumns(int column0, int column1);

    M transpose();

//    M deepTranspose();

    boolean isSquare();
}
