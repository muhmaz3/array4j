#include "com_googlecode_array4j_kernel_NativeDoubleFunctions.h"

#include <iostream>

JNIEXPORT void JNICALL Java_com_googlecode_array4j_kernel_NativeDoubleFunctions_setitem(JNIEnv* env, jobject, jdouble value, jobject data, jint offset) {
    jbyte* const buffer = (jbyte*) env->GetDirectBufferAddress(data);
    if (buffer == NULL) {
        env->FatalError("invalid buffer");
    }
    *((jdouble*) &buffer[offset]) = value;
}

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
