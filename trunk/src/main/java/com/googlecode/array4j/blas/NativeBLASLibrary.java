package com.googlecode.array4j.blas;

import java.nio.FloatBuffer;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface NativeBLASLibrary extends Library {
    NativeBLASLibrary INSTANCE = (NativeBLASLibrary) Native.loadLibrary("array4j", NativeBLASLibrary.class);

    void array4j_sgemm(int order, int transa, int transb, int m, int n, int k, float alpha, FloatBuffer a, int lda,
            FloatBuffer b, int ldb, float beta, FloatBuffer c, int ldc);
}
