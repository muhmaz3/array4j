package com.googlecode.array4j.dense;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import java.nio.Buffer;
import java.util.List;

public interface DenseMatrix extends Matrix {
    Buffer data();

    int leadingDimension();

    int offset();

    Order order();

    Storage storage();

    int stride();

    DenseMatrix transpose();

    Iterable<? extends DenseVector> columnsIterator();

    List<? extends DenseVector> columnsList();

    DenseVector row(int row);

    DenseVector column(int column);

    Iterable<? extends DenseVector> rowsIterator();
}
