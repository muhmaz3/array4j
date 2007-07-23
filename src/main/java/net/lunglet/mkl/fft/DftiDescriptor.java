package net.lunglet.mkl.fft;

import java.nio.Buffer;

// TODO implement copyDescriptor if it could be useful

public final class DftiDescriptor {
    static {
        System.loadLibrary("array4j");
    }

    private static native long commitDescriptor(long handle);

    private static native long computeBackward(long handle, Buffer inout, int offset);

    private static native long computeBackward(long handle, Buffer in, int inoff, Buffer out, int outoff);

    private static native long computeForward(long handle, Buffer inout, int offset);

    private static native long computeForward(long handle, Buffer in, int inoff, Buffer out, int outoff);

    private static native long createDescriptor(long[] handleHolder, int precision, int forwardDomain, int[] length);

    private static native long freeDescriptor(long handle);

    private static native float getFloatValue(long handle, int param, long[] statusHolder);

    private static native int[] getIntArrayValue(long handle, int param, long[] statusHolder);

    private static native int getIntValue(long handle, int param, long[] statusHolder);

    private static native String getStringValue(long handle, int param, long[] statusHolder);

    private static native long setValue(long handle, int param, float value);

    private static native long setValue(long handle, int param, int value);

    private static native long setValue(long handle, int param, int[] value);

    private static native long setValue(long handle, int param, String value);

    private final long handle;

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
        long status = createDescriptor(handleHolder, precision.intValue(), forwardDomain.intValue(), lengths);
        DftiError.checkStatus(status);
        this.handle = handleHolder[0];
        // initialize descriptor name to empty string
        setValue(DftiConfigParam.DESCRIPTOR_NAME, "");
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
        long status = commitDescriptor(handle);
        DftiError.checkStatus(status);
    }

    /**
     * Compute backward transform in-place.
     * @param inout input/output buffer
     * @param offset offset in bytes
     * @throws DftiException
     */
    public void computeBackward(final Buffer inout, final int offset) throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.INPLACE)) {
            throw new DftiException("Must use inplace placement");
        }
        // TODO check that buffer is large enough to support calculation
        long status = computeBackward(handle, inout, offset);
        DftiError.checkStatus(status);
    }

    /**
     * Compute backward transform out-of-place.
     * @param in input buffer
     * @param inoff input buffer offset in bytes
     * @param out output buffer
     * @param outoff output buffer offset in bytes
     * @throws DftiException
     */
    public void computeBackward(final Buffer in, final int inoff, final Buffer out, final int outoff)
            throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.NOT_INPLACE)) {
            throw new DftiException("Must not use inplace placement");
        }
        // TODO check that buffers are large enough to support calculation
        long status = computeBackward(handle, in, inoff, out, outoff);
        DftiError.checkStatus(status);
    }

    /**
     * Compute forward transform in-place.
     * @param inout input/output buffer
     * @param offset offset in bytes
     * @throws DftiException
     */
    public void computeForward(final Buffer inout, final int offset) throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.INPLACE)) {
            throw new DftiException("Must use inplace placement");
        }
        // TODO check that buffer is large enough to support calculation
        long status = computeForward(handle, inout, offset);
        DftiError.checkStatus(status);
    }

    /**
     * Compute forward transform out-of-place.
     * @param in input buffer
     * @param inoff input buffer offset in bytes
     * @param out output buffer
     * @param outoff output buffer offset in bytes
     * @throws DftiException
     */
    public void computeForward(final Buffer in, final int inoff, final Buffer out, final int outoff)
            throws DftiException {
        if (!getValue(DftiConfigParam.PLACEMENT).equals(DftiConfigValue.NOT_INPLACE)) {
            throw new DftiException("Must not use inplace placement");
        }
        // TODO check that buffers are large enough to support calculation
        long status = computeForward(handle, in, inoff, out, outoff);
        DftiError.checkStatus(status);
    }

    /**
     * Free resources allocated to the descriptor.
     */
    public void free() {
        freeDescriptor(handle);
    }

    public double getFloatValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.FLOAT_SCALAR)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
        double value = getFloatValue(handle, param.intValue(), status);
        DftiError.checkStatus(status[0]);
        return value;
    }

    public int[] getIntArrayValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.INT_ARRAY)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
        int[] value = getIntArrayValue(handle, param.intValue(), status);
        DftiError.checkStatus(status[0]);
        return value;
    }

    public int getIntValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.INT_SCALAR)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
        int value = getIntValue(handle, param.intValue(), status);
        DftiError.checkStatus(status[0]);
        return value;
    }

    public String getStringValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.STRING)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
        String value = getStringValue(handle, param.intValue(), status);
        DftiError.checkStatus(status[0]);
        return value;
    }

    public DftiConfigValue getValue(final DftiConfigParam param) throws DftiException {
        if (!param.type.equals(DftiConfigParamType.NAMED_CONSTANT)) {
            throw new IllegalArgumentException();
        }
        long[] status = new long[1];
        int intValue = getIntValue(handle, param.intValue(), status);
        DftiError.checkStatus(status[0]);
        for (DftiConfigValue value : DftiConfigValue.values()) {
            if (value.intValue() == intValue) {
                return value;
            }
        }
        // This should never happen. If it did, it means the MKL library
        // returned a value for a named constant that hasn't been mapped to a
        // value in the DftiConfigValue enum.
        throw new AssertionError();
    }

    public void setValue(final DftiConfigParam param, final DftiConfigValue value) throws DftiException {
        setValue(param, value.intValue());
    }

    public void setValue(final DftiConfigParam param, final float value) throws DftiException {
        long status = setValue(handle, param.intValue(), value);
        DftiError.checkStatus(status);
    }

    public void setValue(final DftiConfigParam param, final int value) throws DftiException {
        long status = setValue(handle, param.intValue(), value);
        DftiError.checkStatus(status);
    }

    public void setValue(final DftiConfigParam param, final int[] value) throws DftiException {
        long status = setValue(handle, param.intValue(), value);
        DftiError.checkStatus(status);
    }

    public void setValue(final DftiConfigParam param, final String value) throws DftiException {
        long status = setValue(handle, param.intValue(), value);
        DftiError.checkStatus(status);
    }
}
