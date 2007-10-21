package net.lunglet.fft.mkl;

import java.nio.Buffer;

//JNIEXPORT jintArray JNICALL Java_net_lunglet_mkl_fft_DftiDescriptor_getIntArrayValue
//  (JNIEnv *env, jclass clazz, jlong handle, jint param, jlongArray statusHolder)
//{
//    long status = -1;
//    jsize dimension;
//    jintArray value;
//    jint *value1;
//    status = DftiGetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), DFTI_DIMENSION, &dimension);
//    if (!DftiErrorClass(status, DFTI_NO_ERROR)) {
//        (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
//        return NULL;
//    }
//    // array has an extra element for these parameters
//    if (param == DFTI_INPUT_STRIDES || param == DFTI_OUTPUT_STRIDES) {
//        dimension++;
//    }
//    value = (*env)->NewIntArray(env, dimension);
//    value1 = (*env)->GetPrimitiveArrayCritical(env, value, NULL);
//    status = DftiGetValue(DFTI_DESCRIPTOR_HANDLE_PTR(handle), param, value1);
//    (*env)->ReleasePrimitiveArrayCritical(env, value, value1, JNI_ABORT);
//    (*env)->SetLongArrayRegion(env, statusHolder, 0, 1, (const jlong*) &status);
//    return value;
//}

public final class DftiDescriptor {
    /**
     * Constructor.
     *
     * @param precision
     *                <CODE>DftiConfigValue.SINGLE</CODE> or
     *                <CODE>DftiConfigValue.DOUBLE</CODE>
     * @param forwardDomain
     *                <CODE>DftiConfigValue.COMPLEX</CODE>,
     *                <CODE>DftiConfigValue.REAL</CODE> or
     *                <CODE>DftiConfigValue.CONJUGATE_EVEN</CODE>
     * @param lengths
     *                lengths of the input dimensions
     * @throws DftiException
     */
    public DftiDescriptor(final DftiConfigValue precision, final DftiConfigValue forwardDomain, final int[] lengths)
            throws DftiException {
        if (lengths.length < 1) {
            throw new IllegalArgumentException();
        }
        long[] handleHolder = new long[1];
//        long status = createDescriptor(handleHolder, precision.intValue(), forwardDomain.intValue(), lengths);
//        DftiError.checkStatus(status);
//        this.handle = handleHolder[0];
        // TODO initialize descriptor name to empty string
        // TODO this still crashes
//        setValue(DftiConfigParam.DESCRIPTOR_NAME, "");
    }

    /**
     * Commit changes to descriptor.
     * <p>
     * This method must be called before using the descriptor to compute
     * transforms.
     *
     * @throws DftiException
     */
    public void commit() throws DftiException {
        // TODO what happens when commit is called more than once?
//        long status = commitDescriptor(handle);
//        DftiError.checkStatus(status);
    }

    /**
     * Compute backward transform in-place.
     * @param inout input/output buffer
     * @throws DftiException
     */
    public void computeBackward(final Buffer inout) throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.INPLACE)) {
            throw new DftiException("Must use inplace placement");
        }
        // TODO check that buffer is large enough to support calculation
//        long status = computeBackward(handle, inout);
//        DftiError.checkStatus(status);
    }

    /**
     * Compute backward transform out-of-place.
     * @param in input buffer
     * @param out output buffer
     * @throws DftiException
     */
    public void computeBackward(final Buffer in, final Buffer out)
            throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.NOT_INPLACE)) {
            throw new DftiException("Must not use inplace placement");
        }
        // TODO check that buffers are large enough to support calculation
//        long status = computeBackward(handle, in, out);
//        DftiError.checkStatus(status);
    }

    /**
     * Compute forward transform in-place.
     * @param inout input/output buffer
     * @throws DftiException
     */
    public void computeForward(final Buffer inout) throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.INPLACE)) {
            throw new DftiException("Must use inplace placement");
        }
        // TODO check that buffer is large enough to support calculation
//        long status = computeForward(handle, inout);
//        DftiError.checkStatus(status);
    }

    /**
     * Compute forward transform out-of-place.
     * @param in input buffer
     * @param out output buffer
     * @throws DftiException
     */
    public void computeForward(final Buffer in, final Buffer out) throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.NOT_INPLACE)) {
            throw new DftiException("Must not use inplace placement");
        }
        // TODO check that buffers are large enough to support calculation
//        long status = computeForward(handle, in, out);
//        DftiError.checkStatus(status);
    }

    /**
     * Free resources allocated to the descriptor.
     */
    public void free() {
//        freeDescriptor(handle);
    }

    public double getFloatValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.FLOAT_SCALAR)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
//        double value = getFloatValue(handle, param.intValue(), status);
//        DftiError.checkStatus(status[0]);
//        return value;
        return 0.0;
    }

    public int[] getIntArrayValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.INT_ARRAY)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
//        int[] value = getIntArrayValue(handle, param.intValue(), status);
//        DftiError.checkStatus(status[0]);
//        return value;
        return null;
    }

    public int getIntValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.INT_SCALAR)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
//        int value = getIntValue(handle, param.intValue(), status);
//        DftiError.checkStatus(status[0]);
//        return value;
        return 0;
    }

    public String getStringValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.STRING)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
//        String value = getStringValue(handle, param.intValue(), status);
//        DftiError.checkStatus(status[0]);
//        return value;
        return null;
    }

    public DftiConfigValue getValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.NAMED_CONSTANT)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
//        int intValue = getIntValue(handle, param.intValue(), status);
//        DftiError.checkStatus(status[0]);
//        for (DftiConfigValue value : DftiConfigValue.values()) {
//            if (value.intValue() == intValue) {
//                return value;
//            }
//        }
        // This should never happen. If it did, it means the MKL library
        // returned a value for a named constant that hasn't been mapped to a
        // value in the DftiConfigValue enum.
        throw new AssertionError();
    }

    public void setValue(final DftiConfigParam param, final DftiConfigValue value) throws DftiException {
        setValue(param, value.intValue());
    }

    public void setValue(final DftiConfigParam param, final float value) throws DftiException {
//        long status = setValue(handle, param.intValue(), value);
//        DftiError.checkStatus(status);
    }

    public void setValue(final DftiConfigParam param, final int value) throws DftiException {
//        long status = setValue(handle, param.intValue(), value);
//        DftiError.checkStatus(status);
    }

    public void setValue(final DftiConfigParam param, final int[] value) throws DftiException {
//        long status = setValue(handle, param.intValue(), value);
//        DftiError.checkStatus(status);
    }

    public void setValue(final DftiConfigParam param, final String value) throws DftiException {
//        long status = setValue(handle, param.intValue(), value);
//        DftiError.checkStatus(status);
    }
}
