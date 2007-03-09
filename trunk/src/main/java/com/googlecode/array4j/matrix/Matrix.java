package com.googlecode.array4j.matrix;

import com.googlecode.array4j.Array;

public interface Matrix<E extends Matrix> extends Array<E> {
    int rows();

    int columns();
}
