#include "com_googlecode_array4j_FloatBLAS.h"
#include "net_lunglet_mkl_fft_DftiDescriptor.h"

#include <mkl.h>
#include <stdlib.h>
#include <string.h>

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

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_createDescriptor
  (JNIEnv *env, jclass clazz, jlongArray handle, jint precision, jint forwardDomain, jintArray length)
{
    jlong status = -1;
    DFTI_DESCRIPTOR_HANDLE handle1 = NULL;
    jsize dimension = (*env)->GetArrayLength(env, length);
    jint *length1 = NULL;
    // create a new array to put into the descriptor
    jint *length2 = malloc(sizeof(jint) * dimension);
    jsize i;
    // copy lengths array
    length1 = (*env)->GetPrimitiveArrayCritical(env, length, NULL);
    for (i = 0; i < dimension; i++) {
        length2[i] = length1[i];
    }
    (*env)->ReleasePrimitiveArrayCritical(env, length, length1, JNI_ABORT);
    length1 = NULL;
    status = DftiCreateDescriptor(&handle1, precision, forwardDomain, dimension, length2);
    (*env)->SetLongArrayRegion(env, handle, 0, 1, (const jlong*) &handle1);
    return status;
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_commitDescriptor
  (JNIEnv *env, jclass clazz, jlong handle)
{
    return DftiCommitDescriptor((DFTI_DESCRIPTOR_HANDLE) handle);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_freeDescriptor
  (JNIEnv *env, jclass clazz, jlong handle)
{
    DFTI_DESCRIPTOR_HANDLE handle1 = (DFTI_DESCRIPTOR_HANDLE) handle;
    // TODO make sure this frees the copy of the lengths array
    return DftiFreeDescriptor(&handle1);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JII
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jint value)
{
    return DftiSetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, value);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JIF
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jfloat value)
{
    return DftiSetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, value);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JI_3I
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jintArray value)
{
    jlong status = -1;
    jint *value1 = (*env)->GetPrimitiveArrayCritical(env, value, NULL);
    status = DftiSetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, value1);
    (*env)->ReleasePrimitiveArrayCritical(env, value, value1, JNI_ABORT);
    return status;
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JILjava_lang_String_2
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jstring value)
{
    return DftiSetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, (*env)->GetStringUTFChars(env, value, NULL));
}

JNIEXPORT jboolean JNICALL Java_net_lunglet_mkl_fft_DftiError_errorClass
  (JNIEnv *env, jclass clazz, jlong status, jlong errorClass)
{
    return DftiErrorClass((long) status, (long) errorClass) != 0;
}

JNIEXPORT jstring JNICALL Java_net_lunglet_mkl_fft_DftiError_errorMessage
  (JNIEnv *env, jclass clazz, jlong status)
{
    char* message = DftiErrorMessage((long) status);
    return (*env)->NewStringUTF(env, message);
}

JNIEXPORT jint JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getIntValue
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jlongArray statusHolder)
{
    jint value;
    jlong status = DftiGetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, &value);
    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
    return value;
}

JNIEXPORT jfloat JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getFloatValue
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jlongArray statusHolder)
{
    jfloat value;
    jlong status = DftiGetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, &value);
    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
    return value;
}

JNIEXPORT jstring JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getStringValue
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jlongArray statusHolder)
{
    char value[DFTI_VERSION_LENGTH];
    jlong status = DftiGetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, value);
    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
    return (*env)->NewStringUTF(env, value);
}

JNIEXPORT jintArray JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getIntArrayValue
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jlongArray statusHolder)
{
    long status = -1;
    jsize dimension;
    jintArray value;
    jint *value1;
    status = DftiGetValue((DFTI_DESCRIPTOR_HANDLE) handle, DFTI_DIMENSION, &dimension);
    if (!DftiErrorClass(status, DFTI_NO_ERROR)) {
        (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
        return NULL;
    }
    value = (*env)->NewIntArray(env, dimension);
    value1 = (*env)->GetPrimitiveArrayCritical(env, value, NULL);
    status = DftiGetValue((DFTI_DESCRIPTOR_HANDLE) handle, param, value1);
    (*env)->ReleasePrimitiveArrayCritical(env, value, value1, JNI_ABORT);
    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
    return value;
}
