package com.googlecode.array4j;

public interface Matrix<M extends Matrix, V extends Vector> extends Array<M> {
    int rows();

    int columns();

    V row(int row);

    V column(int column);

    Iterable<V> rowsIterator();

    Iterable<V> columnsIterator();

    M transpose();

    V createRowVector();

    V createColumnVector();
}
