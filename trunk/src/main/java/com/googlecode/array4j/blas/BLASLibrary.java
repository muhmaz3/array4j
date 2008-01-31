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
                // if OMP_NUM_THREADS contains an invalid value the library will
                // choose the number of threads, so synchronize in this case
                openmpThreads = Integer.MAX_VALUE;
            }
            if (openmpThreads > 1) {
                return (BLASLibrary) Native.synchronizedLibrary(lib);
            }
            return (BLASLibrary) lib;
        }
    }

    BLASLibrary INSTANCE = new Loader().loadLibrary();

    void array4j_log(int n, FloatBuffer x, int incx, FloatBuffer y, int incy);

    void array4j_saxpy(int n, float a, FloatBuffer x, int incx, FloatBuffer y, int incy);

    float array4j_sdot(int n, FloatBuffer x, int incx, FloatBuffer y, int incy);

    void array4j_sgemv(int order, int trans, int m, int n, float alpha, FloatBuffer a, int lda, FloatBuffer x,
            int incx, float beta, FloatBuffer y, int incy);

    void array4j_sgemm(int order, int transa, int transb, int m, int n, int k, float alpha, FloatBuffer a, int lda,
            FloatBuffer b, int ldb, float beta, FloatBuffer c, int ldc);

    void array4j_sscal(int n, float a, FloatBuffer x, int incx);

    void array4j_ssyrk(int order, int uplo, int trans, int n, int k, float alpha, FloatBuffer a, int lda, float beta,
            FloatBuffer c, int ldc);
}
