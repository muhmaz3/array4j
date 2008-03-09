#ifdef __cplusplus
extern "C"
{
#endif

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct gmm {
    int dimension;
    int mixtures;
    float* means;
    float* precisions;
    float* logweights;
    float* lloffsets;
    float* work;
} gmm_t;

typedef struct gmm_stats {
    double t;
    float* n;
    float* ex;
    float* exx;
} gmm_stats_t;

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

ARRAY4J_EXPORT void array4j_gmm_joint_ll(const gmm_t* gmm, const float* x)
{
    int i;
    const float* mean = gmm->means;
    const float* precision = gmm->precisions;
    for (i = 0; i < gmm->mixtures; i++) {
        const float* y = x;
        double joint = 0.0;
        int j;
        for (j = 0; j < gmm->dimension; j++) {
            double d = *y++ - *mean++;
            joint -= d * d * *precision++;
        }
        joint *= -0.5;
        joint += gmm->lloffsets[i];
        // add log weight to get joint l.l.
        joint += gmm->logweights[i];
        gmm->work[i] = (float) joint;
    }
}

ARRAY4J_EXPORT void array4j_gmm_posteriors(const gmm_t* gmm, const float* x)
{
    double sum = 0.0f;
    size_t maxindex;
    float* pr = gmm->work;
    double max;
    int i;
    double marginal;
    array4j_gmm_joint_ll(gmm, x);
    // calculate marginal l.l. using logsumexp trick
    maxindex = cblas_isamax(gmm->mixtures, pr, 1);
    max = pr[maxindex];
    for (i = 0; i < gmm->mixtures; i++) {
        sum += exp(pr[i] - max);
    }
    marginal = max + log(sum);
    // subtract marginal l.l. from joint l.l. to get log posteriors
    for (i = 0; i < gmm->mixtures; i++) {
        pr[i] -= marginal;
    }
    // convert posteriors to linear domain
    vsExp(gmm->mixtures, pr, pr);
}

ARRAY4J_EXPORT void array4j_gmm_map_e(gmm_t* gmm, gmm_stats_t* stats, int n, const float* data)
{
    int k;
    const float* x = data;
    const int m = gmm->dimension * gmm->mixtures;

    // reset stats
    stats->t = 0.0;
    memset(stats->n, 0, sizeof(float) * gmm->mixtures);
    memset(stats->ex, 0, sizeof(float) * m);
    memset(stats->exx, 0, sizeof(float) * m);

    for (k = 0; k < n; k++) {
        int i;
        float* ex = stats->ex;
        float* exx = stats->exx;
        array4j_gmm_posteriors(gmm, x);
        for (i = 0; i < gmm->mixtures; i++) {
            int j;
            float n = gmm->work[i];
            stats->n[i] += n;
            for (j = 0; j < gmm->dimension; j++) {
                float y = x[j];
                float ny = n * y;
                *ex++ += ny;
                *exx++ += ny * y;
            }
        }
        x += gmm->dimension;
    }
    stats->t += n;
}

ARRAY4J_EXPORT void array4j_gmm_map_m(gmm_t* gmm, gmm_stats_t* stats)
{
}

ARRAY4J_EXPORT void array4j_gmm_stats_init(gmm_stats_t* stats, float* n, float* ex, float* exx)
{
    stats->t = 0.0;
    stats->n = n;
    stats->ex = ex;
    stats->exx = exx;
}

ARRAY4J_EXPORT void array4j_gmm_stats_merge(gmm_stats_t* stats1, const gmm_stats_t* stats2)
{
}

ARRAY4J_EXPORT void array4j_gmm_print(const gmm_t* gmm)
{
    int i;
    printf("dimension = %d\n", gmm->dimension);
    printf("mixtures = %d\n", gmm->mixtures);
    printf("work = %p\n", gmm->work);
    for (i = 0; i < gmm->mixtures; i++) {
        int j;
        float* mean = &gmm->means[i * gmm->dimension];
        float* precision = &gmm->precisions[i * gmm->dimension];
        float logweight = gmm->logweights[i];
        printf("mean = ");
        for (j = 0; j < gmm->dimension; j++) {
            float u = *(mean + j);
            printf("%.8f ", u);
        }
        printf("\n");
        printf("precision = ");
        for (j = 0; j < gmm->dimension; j++) {
            float p = *(precision + j);
            printf("%.8f ", p);
        }
        printf("\n");
        printf("logweight = %.8f\n", logweight);
    }
};

ARRAY4J_EXPORT void array4j_gmm_init(gmm_t* gmm, float* means, float* precisions, float* logweights, float* lloffsets, float* work)
{
    float dimoff = -0.5f * gmm->dimension * logf(2.0f * M_PI);
    int i;
    gmm->means = means;
    gmm->precisions = precisions;
    gmm->logweights = logweights;
    gmm->lloffsets = lloffsets;
    gmm->work = work;
    for (i = 0; i < gmm->mixtures; i++) {
        int j;
        float* precision = &gmm->precisions[i * gmm->dimension];
        gmm->lloffsets[i] = dimoff;
        for (j = 0; j < gmm->dimension; j++) {
            float p = *(precision + j);
            gmm->lloffsets[i] += 0.5f * logf(p);
        }
    }
}

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

#ifdef __cplusplus
}
#endif
