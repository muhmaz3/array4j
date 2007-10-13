#include <mkl.h>
#include <stdlib.h>

#ifdef _WIN32
#define ARRAY4J_EXPORT __declspec(dllexport)
#else
#define ARRAY4J_EXPORT
#endif

#if 0
ARRAY4J_EXPORT long array4j_mkl_DftiCreateDescriptor(DFTI_DESCRIPTOR_HANDLE* handle)
{
    return DftiCommitDescriptor(handle);
}

ARRAY4J_EXPORT long array4j_mkl_DftiCommitDescriptor(DFTI_DESCRIPTOR_HANDLE handle)
{
    return DftiCommitDescriptor(handle);
}

ARRAY4J_EXPORT long array4j_mkl_DftiFreeDescriptor(DFTI_DESCRIPTOR_HANDLE* handle)
{
    return DftiFreeDescriptor(handle);
}

ARRAY4J_EXPORT char* array4j_mkl_DftiErrorMessage(long i)
{
    return DftiErrorMessage(i);
}

ARRAY4J_EXPORT long array4j_mkl_DftiErrorClass(long i, long j)
{
    return DftiErrorClass(i, j);
}
#endif

ARRAY4J_EXPORT void array4j_sgemm
  (int order, int transa, int transb, int m, int n, int k, float alpha, const float* a, int lda, const float* b, int ldb, float beta, float *c, int ldc)
{
    cblas_sgemm(order, transa, transb, m, n, k, alpha, a, lda, b, ldb, beta, c, ldc);
}

ARRAY4J_EXPORT void array4j_ssyrk
  (int order, int uplo, int trans, int n, int k, float alpha, const float* a, int lda, float beta, float *c, int ldc)
{
    cblas_ssyrk(order, uplo, trans, n, k, alpha, a, lda, beta, c, ldc);
}

ARRAY4J_EXPORT float array4j_sdot
  (int n, const float* x, int incx, const float *y, int incy)
{
    return cblas_sdot(n, x, incx, y, incy);
}

ARRAY4J_EXPORT void array4j_saxpy
  (int n, float a, const float* x, int incx, float *y, int incy)
{
    cblas_saxpy(n, a, x, incx, y, incy);
}

ARRAY4J_EXPORT void array4j_log
  (int n, const float* x, int incx, float* y, int incy)
{
    float* a = incx == 1 ? x : malloc(n * sizeof(float));
    float* r = incy == 1 ? y : malloc(n * sizeof(float));
    if (incx != 1) {
        vsPackI(n, x, incx, a);
    }
    vsLn(n, a, r);
    if (incx != 1) {
        free(a);
    }
    if (incy != 1) {
        vsUnpackI(n, r, y, incy);
        free(r);
    }
}
