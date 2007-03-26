#include "com_googlecode_array4j_kernel_NativeDoubleFunctions.h"

#include <iostream>

JNIEXPORT void JNICALL Java_com_googlecode_array4j_kernel_NativeDoubleFunctions_fill(JNIEnv* env, jobject, jobject data, jint length) {
    jdouble* const buffer = (jdouble*) env->GetDirectBufferAddress(data);
    if (buffer == NULL) {
        env->FatalError("invalid buffer");
    }
    jdouble const start = buffer[0];
    jdouble const delta = buffer[1] - start;
    for (register jint i = 2; i < length; ++i) {
        buffer[i] = start + i * delta;
    }
}

JNIEXPORT jdouble JNICALL Java_com_googlecode_array4j_kernel_NativeDoubleFunctions_getitemDouble(JNIEnv* env, jobject, jobject data, jint offset) {
    jbyte* const buffer = (jbyte*) env->GetDirectBufferAddress(data);
    if (buffer == NULL) {
        env->FatalError("invalid buffer");
    }
    return *((jdouble*) &buffer[offset]);
}

JNIEXPORT void JNICALL Java_com_googlecode_array4j_kernel_NativeDoubleFunctions_setitemDouble(JNIEnv* env, jobject, jdouble value, jobject data, jint offset) {
    jbyte* const buffer = (jbyte*) env->GetDirectBufferAddress(data);
    if (buffer == NULL) {
        env->FatalError("invalid buffer");
    }
    *((jdouble*) &buffer[offset]) = value;
}

JNIEXPORT void JNICALL Java_com_googlecode_array4j_kernel_NativeDoubleFunctions_add(JNIEnv* env, jobject, jobjectArray bufptr, jintArray offsets, jintArray dimensions, jintArray steps) {
    jbyte* i1 = (jbyte*) env->GetDirectBufferAddress(env->GetObjectArrayElement(bufptr, 0));
    jbyte* i2 = (jbyte*) env->GetDirectBufferAddress(env->GetObjectArrayElement(bufptr, 1));
    jbyte* op = (jbyte*) env->GetDirectBufferAddress(env->GetObjectArrayElement(bufptr, 2));
    if (i1 == NULL || i2 == NULL || op == NULL) {
        env->FatalError("invalid buffer");
    }

    jint* const offp = (jint*) env->GetPrimitiveArrayCritical(offsets, 0);
    if (offp == NULL) {
        env->FatalError("invalid array");
    }
    i1 = &i1[offp[0]];
    i2 = &i2[offp[1]];
    op = &op[offp[2]];
    env->ReleasePrimitiveArrayCritical(offsets, offp, 0);

    jint* const stepsp = (jint*) env->GetPrimitiveArrayCritical(steps, 0);
    if (stepsp == NULL) {
        env->FatalError("invalid array");
    }
    jint const is1 = stepsp[0];
    jint const is2 = stepsp[1];
    jint const os = stepsp[2];
    env->ReleasePrimitiveArrayCritical(steps, stepsp, 0);

    jint* const dimsp = (jint*) env->GetPrimitiveArrayCritical(dimensions, 0);
    if (dimsp == NULL) {
        env->FatalError("invalid array");
    }
    jint const n = dimsp[0];
    env->ReleasePrimitiveArrayCritical(dimensions, dimsp, 0);

    for (register jint i=0; i < n; i++, i1 += is1, i2 += is2, op += os) {
        *((jdouble *)op) = *((jdouble *)i1) + *((jdouble *)i2);
    }
}

JNIEXPORT void JNICALL Java_com_googlecode_array4j_kernel_NativeDoubleFunctions_multiply(JNIEnv* env, jobject, jobjectArray bufptr, jintArray offsets, jintArray dimensions, jintArray steps) {
    jbyte* i1 = (jbyte*) env->GetDirectBufferAddress(env->GetObjectArrayElement(bufptr, 0));
    jbyte* i2 = (jbyte*) env->GetDirectBufferAddress(env->GetObjectArrayElement(bufptr, 1));
    jbyte* op = (jbyte*) env->GetDirectBufferAddress(env->GetObjectArrayElement(bufptr, 2));
    if (i1 == NULL || i2 == NULL || op == NULL) {
        env->FatalError("invalid buffer");
    }

    jint* const offp = (jint*) env->GetPrimitiveArrayCritical(offsets, 0);
    if (offp == NULL) {
        env->FatalError("invalid array");
    }
    i1 = &i1[offp[0]];
    i2 = &i2[offp[1]];
    op = &op[offp[2]];
    env->ReleasePrimitiveArrayCritical(offsets, offp, 0);

    jint* const stepsp = (jint*) env->GetPrimitiveArrayCritical(steps, 0);
    if (stepsp == NULL) {
        env->FatalError("invalid array");
    }
    jint const is1 = stepsp[0];
    jint const is2 = stepsp[1];
    jint const os = stepsp[2];
    env->ReleasePrimitiveArrayCritical(steps, stepsp, 0);

    jint* const dimsp = (jint*) env->GetPrimitiveArrayCritical(dimensions, 0);
    if (dimsp == NULL) {
        env->FatalError("invalid array");
    }
    jint const n = dimsp[0];
    env->ReleasePrimitiveArrayCritical(dimensions, dimsp, 0);

    for (register jint i=0; i < n; i++, i1 += is1, i2 += is2, op += os) {
        *((jdouble *)op) = *((jdouble *)i1) * *((jdouble *)i2);
    }
}
