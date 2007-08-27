package com.googlecode.array4j.dense;

import java.nio.Buffer;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;

public interface DenseMatrix<M extends DenseMatrix<M, V>, V extends DenseVector<V>> extends Matrix<M, V> {
    int offset();

    Orientation orientation();

    Storage storage();

    int stride();

    Buffer data();
}
