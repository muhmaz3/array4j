#include "net_lunglet_mkl_fft_DftiDescriptor.h"
#include "net_lunglet_mkl_fft_DftiError.h"

#include <mkl.h>
#include <stdlib.h>
#include <string.h>

/*
// Converting between pointers and 64-bit integers.
//
// CAUTION: assuming that void* occupies 8 bytes or less!
*/

union PtrHack {
    jlong l;
    void* p;
};

static jlong jlong4ptr(void* ptr) {
    union PtrHack hack;
    hack.p = ptr;
    return hack.l;
}

static void* ptr4jlong(jlong l) {
    union PtrHack hack;
    hack.l = l;
    return hack.p;
}

#define DFTI_DESCRIPTOR_HANDLE_PTR(x) ((DFTI_DESCRIPTOR_HANDLE) ptr4jlong(x))

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_createDescriptor
  (JNIEnv *env, jclass clazz, jlongArray handle, jint precision, jint forwardDomain, jintArray lengths)
{
    jlong status = -1;
    DFTI_DESCRIPTOR_HANDLE handle1 = NULL;
    jlong handle1_hack;
    jsize dimension = (*env)->GetArrayLength(env, lengths);
    jint *lengths1 = NULL;
    lengths1 = (*env)->GetPrimitiveArrayCritical(env, lengths, NULL);
    if (dimension == 1) {
        status = DftiCreateDescriptor(&handle1, precision, forwardDomain, dimension, lengths1[0]);
    } else {
        jint lengths2[7];
        jsize i;
        if (dimension > 7) {
            (*env)->FatalError(env, "wrong dimension");
        }
        for (i = 0; i < dimension; i++) {
            lengths2[i] = lengths1[i];
        }
        status = DftiCreateDescriptor(&handle1, precision, forwardDomain, dimension, lengths2);
    }
    (*env)->ReleasePrimitiveArrayCritical(env, lengths, lengths1, JNI_ABORT);
    handle1_hack = jlong4ptr(handle1);
    (*env)->SetLongArrayRegion(env, handle, 0, 1, (const jlong*) &handle1_hack);
    return status;
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_commitDescriptor
  (JNIEnv *env, jclass clazz, jlong handle)
{
    return DftiCommitDescriptor(DFTI_DESCRIPTOR_HANDLE_PTR(handle));
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_freeDescriptor
  (JNIEnv *env, jclass clazz, jlong handle)
{
    DFTI_DESCRIPTOR_HANDLE handle1 = DFTI_DESCRIPTOR_HANDLE_PTR(handle);
    return DftiFreeDescriptor(&handle1);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JII
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jint value)
{
    return DftiSetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, value);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JIF
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jfloat value)
{
    return DftiSetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, value);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JI_3I
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jintArray value)
{
    jlong status = -1;
    jint *value1 = (*env)->GetPrimitiveArrayCritical(env, value, NULL);
    status = DftiSetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, value1);
    (*env)->ReleasePrimitiveArrayCritical(env, value, value1, JNI_ABORT);
    return status;
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_setValue__JILjava_lang_String_2
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jstring value)
{
    const char* str;
    jlong status;
    str = (*env)->GetStringUTFChars(env, value, NULL);
    if (str == NULL) {
        (*env)->FatalError(env, "str == NULL");
    }
    status = DftiSetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, str);
    (*env)->ReleaseStringUTFChars(env, value, str);
    return status;
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
    jlong status = DftiGetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, &value);
    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
    return value;
}

JNIEXPORT jfloat JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getFloatValue
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jlongArray statusHolder)
{
    jfloat value;
    jlong status = DftiGetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, &value);
    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
    return value;
}

JNIEXPORT jstring JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getStringValue
  (JNIEnv *env, jclass clazz, jlong handle, jint param, jlongArray statusHolder)
{
    char value[DFTI_VERSION_LENGTH];
    jlong status = DftiGetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, value);
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
    status = DftiGetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), DFTI_DIMENSION, &dimension);
    if (!DftiErrorClass(status, DFTI_NO_ERROR)) {
        (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
        return NULL;
    }
    // array has an extra element for these parameters
    if (param == DFTI_INPUT_STRIDES || param == DFTI_OUTPUT_STRIDES) {
        dimension++;
    }
    value = (*env)->NewIntArray(env, dimension);
    value1 = (*env)->GetPrimitiveArrayCritical(env, value, NULL);
    status = DftiGetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, value1);
    (*env)->ReleasePrimitiveArrayCritical(env, value, value1, JNI_ABORT);
    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
    return value;
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeForward__JLjava_nio_Buffer_2
  (JNIEnv *env, jclass clazz, jlong handle, jobject inoutbuf)
{
    char* inout = (*env)->GetDirectBufferAddress(env, inoutbuf);
    if (inout == NULL) {
        (*env)->FatalError(env, "invalid buffer inout");
    }
    return DftiComputeForward(DFTI_DESCRIPTOR_HANDLE_PTR(handle), inout);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeForward__JLjava_nio_Buffer_2Ljava_nio_Buffer_2
  (JNIEnv *env, jclass clazz, jlong handle, jobject inbuf, jobject outbuf)
{
    char* in = (*env)->GetDirectBufferAddress(env, inbuf);
    char* out = (*env)->GetDirectBufferAddress(env, outbuf);
    if (in == NULL) {
        (*env)->FatalError(env, "invalid buffer in");
    }
    if (out == NULL) {
        (*env)->FatalError(env, "invalid buffer out");
    }
    return DftiComputeForward(DFTI_DESCRIPTOR_HANDLE_PTR(handle), in, out);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeBackward__JLjava_nio_Buffer_2
  (JNIEnv *env, jclass clazz, jlong handle, jobject inoutbuf)
{
    char* inout = (*env)->GetDirectBufferAddress(env, inoutbuf);
    if (inout == NULL) {
        (*env)->FatalError(env, "invalid buffer inout");
    }
    return DftiComputeBackward(DFTI_DESCRIPTOR_HANDLE_PTR(handle), inout);
}

JNIEXPORT jlong JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_computeBackward__JLjava_nio_Buffer_2Ljava_nio_Buffer_2
  (JNIEnv *env, jclass clazz, jlong handle, jobject inbuf, jobject outbuf)
{
    char* in = (*env)->GetDirectBufferAddress(env, inbuf);
    char* out = (*env)->GetDirectBufferAddress(env, outbuf);
    if (in == NULL) {
        (*env)->FatalError(env, "invalid buffer in");
    }
    if (out == NULL) {
        (*env)->FatalError(env, "invalid buffer out");
    }
    return DftiComputeBackward(DFTI_DESCRIPTOR_HANDLE_PTR(handle), in, out);
}
