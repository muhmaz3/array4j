package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrix;
import java.nio.FloatBuffer;
import java.util.List;

public interface FloatDenseMatrix extends FloatMatrix, DenseMatrix {
    FloatBuffer data();

    FloatDenseMatrix transpose();

    Iterable<FloatDenseVector> columnsIterator();

    List<FloatDenseVector> columnsList();

    FloatDenseVector row(int row);

    FloatDenseVector column(int column);

    Iterable<FloatDenseVector> rowsIterator();
}
