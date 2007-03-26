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
