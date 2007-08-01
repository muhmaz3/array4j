package com.googlecode.array4j;

public interface FloatVector<V extends FloatVector<V>> extends FloatMatrix<V, V>, Vector<V> {
    float get(int index);

    V minus(FloatVector<?> other);

    void plusEquals(FloatVector<?> other);

    void set(int index, float value);

    void timesEquals(float value);

    float dot(FloatVector<?> other);
}
