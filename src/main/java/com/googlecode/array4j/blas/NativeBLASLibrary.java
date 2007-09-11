package com.googlecode.array4j.blas;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.nio.FloatBuffer;

public interface NativeBLASLibrary extends Library {
    NativeBLASLibrary INSTANCE = (NativeBLASLibrary) Native.loadLibrary("array4j", NativeBLASLibrary.class);

    void array4j_sgemm(int order, int transa, int transb, int m, int n, int k, float alpha, FloatBuffer a, int lda,
            FloatBuffer b, int ldb, float beta, FloatBuffer c, int ldc);

    void array4j_ssyrk(int order, int uplo, int trans, int n, int k, float alpha, FloatBuffer a, int lda, float beta,
            FloatBuffer c, int ldc);
}
