#include "com_googlecode_array4j_blas_DirectFloatBLAS_KernelImpl.h"

#include <mkl.h>

JNIEXPORT jfloat JNICALL Java_com_googlecode_array4j_blas_DirectFloatBLAS_00024KernelImpl_sdot
  (JNIEnv *env, jobject self, jint n, jobject x, jint incx, jobject y, jint incy)
{
    jfloat* xp = (jfloat*) (*env)->GetDirectBufferAddress(env, x);
    jfloat* yp = (jfloat*) (*env)->GetDirectBufferAddress(env, y);
    if (xp == NULL) {
        (*env)->FatalError(env, "invalid buffer x");
    }
    if (xp == NULL) {
        (*env)->FatalError(env, "invalid buffer y");
    }
    return cblas_sdot(n, xp, incx, yp, incy);
}
