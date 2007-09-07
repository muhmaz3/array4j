package com.googlecode.array4j.dense;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import java.nio.Buffer;

public interface DenseMatrix<M extends DenseMatrix<M, V>, V extends DenseVector<V>> extends Matrix<M, V> {
    Buffer data();

    int offset();

    Orientation orientation();

    Storage storage();

    int stride();
}
