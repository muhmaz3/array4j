package com.googlecode.array4j.blas;

import com.googlecode.array4j.dense.FloatDenseVector;

public interface FloatBLAS {
    void copy(FloatDenseVector x, FloatDenseVector y);

    float dot(FloatDenseVector x, FloatDenseVector y);
}
