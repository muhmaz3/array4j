package com.googlecode.array4j;

public interface ComplexFloatVector<V extends ComplexFloatVector<V>> extends ComplexFloatMatrix<V, V>, ComplexVector<V> {
    ComplexFloat get(int index);

    void set(int index, ComplexFloat value);
}
