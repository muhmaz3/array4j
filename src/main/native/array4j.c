#include "com_googlecode_array4j_FloatBLAS.h"

#include <mkl.h>

JNIEXPORT jfloat JNICALL Java_com_googlecode_array4j_FloatBLAS_sdot
  (JNIEnv *env, jclass clazz, jint n, jobject x, jint offx, jint incx, jobject y, jint offy, jint incy)
{
    jfloat* xp = (jfloat*) (*env)->GetDirectBufferAddress(env, x);
    jfloat* yp = (jfloat*) (*env)->GetDirectBufferAddress(env, y);
    if (xp == NULL) {
        (*env)->FatalError(env, "invalid buffer x");
    }
    if (xp == NULL) {
        (*env)->FatalError(env, "invalid buffer y");
    }
    return cblas_sdot(n, &xp[offx], incx, &yp[offy], incy);
}

JNIEXPORT void JNICALL Java_com_googlecode_array4j_FloatBLAS_scopy
  (JNIEnv *env, jclass clazz, jint n, jobject x, jint offx, jint incx, jobject y, jint offy, jint incy)
{
    jfloat* xp = (jfloat*) (*env)->GetDirectBufferAddress(env, x);
    jfloat* yp = (jfloat*) (*env)->GetDirectBufferAddress(env, y);
    if (xp == NULL) {
        (*env)->FatalError(env, "invalid buffer x");
    }
    if (xp == NULL) {
        (*env)->FatalError(env, "invalid buffer y");
    }
    cblas_scopy(n, &xp[offx], incx, &yp[offy], incy);
}
