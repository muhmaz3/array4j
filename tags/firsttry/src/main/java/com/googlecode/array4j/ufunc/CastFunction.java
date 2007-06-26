package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayFunctions;
import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.DenseArray;

public final class CastFunction implements VectorUnaryFunction {
    private final ArrayType fType;

    private final ArrayFunctions funcs;

    public CastFunction(final ArrayType type, final ArrayFunctions funcs) {
        this.fType = type;
        this.funcs = funcs;
    }

    public void call(ByteBuffer srcbuf, ByteBuffer destbuf, int n, DenseArray src, DenseArray dest) {
        throw new UnsupportedOperationException();
    }
}
