package com.googlecode.array4j;

public interface Matrix<E extends Matrix> extends Array<E> {
    double get(final int row, final int column);

    void set(double value, final int row, final int column);

    int getRows();

    int rows();

    int getColumns();

    int columns();
}
