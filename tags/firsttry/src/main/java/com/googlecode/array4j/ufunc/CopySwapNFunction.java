package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.DenseArray;

public interface CopySwapNFunction {
    void copySwapN(ByteBuffer buf0, int stride0, ByteBuffer buf1, int stride1, int n, boolean swap, DenseArray dest);
}
