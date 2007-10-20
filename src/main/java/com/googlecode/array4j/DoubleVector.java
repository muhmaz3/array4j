package com.googlecode.array4j;

public interface DoubleVector<V extends DoubleVector<V>> extends DoubleMatrix<V, V>, Vector<V> {
    double get(int index);

    V minus(DoubleVector<?> other);

    void plusEquals(DoubleVector<?> other);

    void set(int index, double value);
}
