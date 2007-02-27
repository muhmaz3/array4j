package com.googlecode.array4j;

public interface Vector<E extends Vector> extends Matrix<E> {
    double get(int index);

    int length();
}
