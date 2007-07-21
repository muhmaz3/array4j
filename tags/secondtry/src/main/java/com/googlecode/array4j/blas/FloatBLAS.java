package com.googlecode.array4j.blas;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public interface FloatBLAS<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    void axpy(float value, V x, V y);

    void copy(V x, V y);

    float dot(V x, V y);

    int iamax(V x);

    void scal(float value, V x);
}
