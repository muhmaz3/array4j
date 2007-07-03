package com.googlecode.array4j;

public interface Vector<V extends Vector<V>> extends Matrix<V, V> {
    boolean isColumnVector();

    boolean isRowVector();
}
