package com.googlecode.array4j;

import java.util.List;

public interface FloatMatrix extends Matrix, FloatArray {
    FloatVector column(int column);

    Iterable<? extends FloatVector> columnsIterator();

    List<? extends FloatVector> columnsList();

    float get(int row, int column);

    FloatVector row(int row);

    Iterable<? extends FloatVector> rowsIterator();

    void set(int row, int column, float value);

    void setColumn(int column, FloatVector columnVector);

    void setRow(int row, FloatVector rowVector);

    float[][] toColumnArrays();

    float[][] toRowArrays();

    FloatMatrix transpose();
}
