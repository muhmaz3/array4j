package com.googlecode.array4j;

// TODO deepTranspose

public interface Matrix<M extends Matrix<M, V>, V extends Vector<V>> extends Array<M> {
    V asVector();

    V column(int column);

    int columns();

    Iterable<V> columnsIterator();

    V createColumnVector();

    V createRowVector();

    boolean isSquare();

    V row(int row);

    int rows();

    Iterable<V> rowsIterator();

    M transpose();
}
