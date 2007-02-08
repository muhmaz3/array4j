package com.googlecode.array4j;

public interface Matrix<E extends Matrix> extends Array<E> {
    double get(final int row, final int column);

    void set(double value, final int row, final int column);

    int getRows();

    int rows();

    int getColumns();

    int columns();

    RowVector getRow(int index);

    RowVector row(int index);

    RowVector[] getRows(int... indexes);

    ColumnVector getColumn(int index);

    ColumnVector column(int index);

    ColumnVector[] getColumns(int... indexes);
}
