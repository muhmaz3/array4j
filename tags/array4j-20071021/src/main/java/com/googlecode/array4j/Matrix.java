package com.googlecode.array4j;

import java.util.List;

public interface Matrix<M extends Matrix<M, V>, V extends Vector<V>> extends Array<M> {
    V column(int column);

    int columns();

    Iterable<V> columnsIterator();

    List<V> columnsList();

    V createColumnVector();

    V createRowVector();

    boolean isSquare();

    V row(int row);

    int rows();

    Iterable<V> rowsIterator();

    M transpose();
}
