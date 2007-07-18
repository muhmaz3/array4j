package com.googlecode.array4j.blas;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;

public interface FloatBLAS<M extends FloatMatrix<M, V>, V extends FloatVector<V>> {
    float dot(V x, V y);

    float sum(V x);
}
