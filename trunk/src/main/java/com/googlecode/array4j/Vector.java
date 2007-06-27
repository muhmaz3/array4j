package com.googlecode.array4j;

public interface Vector<V extends Vector> extends Matrix<V, V> {
    boolean isColumnVector();

    boolean isRowVector();
}
