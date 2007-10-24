package com.googlecode.array4j;

import java.util.List;

public interface Matrix extends Array {
    Vector column(int column);

    int columns();

    Iterable<? extends Vector> columnsIterator();

    List<? extends Vector> columnsList();

    boolean isSquare();

    Vector row(int row);

    int rows();

    Iterable<? extends Vector> rowsIterator();

    Matrix transpose();
}
