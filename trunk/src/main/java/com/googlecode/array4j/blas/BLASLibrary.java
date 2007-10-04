package com.googlecode.array4j.blas;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.nio.FloatBuffer;

public interface BLASLibrary extends Library {
    static final class Loader {
        private Loader() {
        }

        private BLASLibrary loadLibrary() {
            Library lib = (Library) Native.loadLibrary("array4j", BLASLibrary.class);
            int openmpThreads;
            try {
                openmpThreads = Integer.valueOf(System.getenv("OMP_NUM_THREADS"));
            } catch (NumberFormatException e) {
                openmpThreads = 1;
            }
            if (openmpThreads > 1) {
                return (BLASLibrary) Native.synchronizedLibrary(lib);
            }
            return (BLASLibrary) lib;
        }
    }

    BLASLibrary INSTANCE = new Loader().loadLibrary();

    float array4j_sdot(int n, FloatBuffer x, int incx, FloatBuffer y, int incy);

    void array4j_sgemm(int order, int transa, int transb, int m, int n, int k, float alpha, FloatBuffer a, int lda,
            FloatBuffer b, int ldb, float beta, FloatBuffer c, int ldc);

    void array4j_ssyrk(int order, int uplo, int trans, int n, int k, float alpha, FloatBuffer a, int lda, float beta,
            FloatBuffer c, int ldc);
}
