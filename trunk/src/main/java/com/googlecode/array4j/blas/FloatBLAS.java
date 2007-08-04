package com.googlecode.array4j.blas;

import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

// TODO make a SparseFloatBLAS interface

public interface FloatBLAS {
    /**
     * <CODE>y = x</CODE>.
     */
    void copy(FloatDenseVector x, FloatDenseVector y);

    float dot(FloatDenseVector x, FloatDenseVector y);

    /**
     * <CODE>C = alpha*A*B + beta*C</CODE>.
     */
    void gemm(float alpha, FloatDenseMatrix a, FloatDenseMatrix b, float beta, FloatDenseMatrix c);
}
