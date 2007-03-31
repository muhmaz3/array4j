package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.DenseArray;

public interface VectorUnaryFunction {
    void call(ByteBuffer srcbuf, ByteBuffer destbuf, int n, DenseArray src, DenseArray dest);

}
