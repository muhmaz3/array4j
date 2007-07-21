package com.googlecode.array4j;

public interface Vector<V extends Vector<V>> extends Matrix<V, V> {
    V createVector(int size);

    boolean isColumnVector();

    boolean isRowVector();

    Orientation orientation();
}
