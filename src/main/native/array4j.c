#include <stdlib.h>

#if defined(ARRAY4J_HAVE_MKL)
#include <mkl.h>
#elif defined(ARRAY4J_HAVE_ACML)
#include <acml.h>
typedef void* DFTI_DESCRIPTOR_HANDLE;
#else
#error BLAS/LAPACK includes required
#endif

#ifdef _WIN32
#define ARRAY4J_EXPORT __declspec(dllexport)
#else
#define ARRAY4J_EXPORT
#endif

ARRAY4J_EXPORT long array4j_DftiCreateDescriptor(DFTI_DESCRIPTOR_HANDLE* handle)
{
#if defined(ARRAY4J_HAVE_MKL)
    return 0;
#else
    return 0;
#endif
}

ARRAY4J_EXPORT long array4j_DftiCommitDescriptor(DFTI_DESCRIPTOR_HANDLE handle)
{
#if defined(ARRAY4J_HAVE_MKL)
    return DftiCommitDescriptor(handle);
#else
    return 0;
#endif
}

ARRAY4J_EXPORT long array4j_DftiFreeDescriptor(DFTI_DESCRIPTOR_HANDLE* handle)
{
#if defined(ARRAY4J_HAVE_MKL)
    return DftiFreeDescriptor(handle);
#else
    return 0;
#endif
}

ARRAY4J_EXPORT char* array4j_DftiErrorMessage(long i)
{
#if defined(ARRAY4J_HAVE_MKL)
    return DftiErrorMessage(i);
#else
    return 0;
#endif
}

ARRAY4J_EXPORT long array4j_DftiErrorClass(long i, long j)
{
#if defined(ARRAY4J_HAVE_MKL)
    return DftiErrorClass(i, j);
#else
    return 0;
#endif
}

ARRAY4J_EXPORT void array4j_sgemv
  (int order, int trans, int m, int n, float alpha, float* a, int lda, float* x, int incx, float beta, float *y, int incy)
{
#if defined(ARRAY4J_HAVE_MKL)
    cblas_sgemv(order, trans, m, n, alpha, a, lda, x, incx, beta, y, incy);
#elif defined(ARRAY4J_HAVE_ACML)
    sgemv(trans, m, n, alpha, a, lda, x, incx, beta, y, incy);
#else
#error sgemv function required
#endif
}

ARRAY4J_EXPORT void array4j_sgemm
  (int order, int transa, int transb, int m, int n, int k, float alpha, float* a, int lda, float* b, int ldb, float beta, float *c, int ldc)
{
#if defined(ARRAY4J_HAVE_MKL)
    cblas_sgemm(order, transa, transb, m, n, k, alpha, a, lda, b, ldb, beta, c, ldc);
#elif defined(ARRAY4J_HAVE_ACML)
    sgemm(transa, transb, m, n, k, alpha, a, lda, b, ldb, beta, c, ldc);
#else
#error sgemm function required
#endif
}

ARRAY4J_EXPORT void array4j_ssyrk
  (int order, int uplo, int trans, int n, int k, float alpha, float* a, int lda, float beta, float* c, int ldc)
{
#if defined(ARRAY4J_HAVE_MKL)
    cblas_ssyrk(order, uplo, trans, n, k, alpha, a, lda, beta, c, ldc);
#elif defined(ARRAY4J_HAVE_ACML)
    ssyrk(uplo, trans, n, k, alpha, a, lda, beta, c, ldc);
#else
#error ssyrk function required
#endif
}

ARRAY4J_EXPORT float array4j_sdot
  (int n, float* x, int incx, float* y, int incy)
{
#if defined(ARRAY4J_HAVE_MKL)
    return cblas_sdot(n, x, incx, y, incy);
#elif defined(ARRAY4J_HAVE_ACML)
    return sdot(n, x, incx, y, incy);
#else
#error sdot function required
#endif
}

ARRAY4J_EXPORT void array4j_sscal
  (int n, float a, float* x, int incx)
{
#if defined(ARRAY4J_HAVE_MKL)
    cblas_sscal(n, a, x, incx);
#elif defined(ARRAY4J_HAVE_ACML)
    sscal(n, a, x, incx);
#else
#error sscal function required
#endif
}

ARRAY4J_EXPORT void array4j_saxpy
  (int n, float a, float* x, int incx, float* y, int incy)
{
#if defined(ARRAY4J_HAVE_MKL)
    cblas_saxpy(n, a, x, incx, y, incy);
#elif defined(ARRAY4J_HAVE_ACML)
    saxpy(n, a, x, incx, y, incy);
#else
#error saxpy function required
#endif
}

ARRAY4J_EXPORT void array4j_log
  (int n, const float* x, int incx, float* y, int incy)
{
#if defined(ARRAY4J_HAVE_MKL)
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
#elif defined(ARRAY4J_HAVE_ACML)
#else
#error log function required
#endif
}

ARRAY4J_EXPORT void* array4j_addressof(void* addr)
{
    return addr;
}
