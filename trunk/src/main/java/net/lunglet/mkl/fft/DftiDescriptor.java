package net.lunglet.mkl.fft;

import java.nio.Buffer;

public final class DftiDescriptor {
    static {
        System.loadLibrary("array4j");
    }

    private static native long commitDescriptor(long handle);

    private static native long computeBackward(long handle, Buffer inout);

    private static native long computeBackward(long handle, Buffer in, Buffer out);

    private static native long computeForward(long handle, Buffer inout);

    private static native long computeForward(long handle, Buffer in, Buffer out);

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

    public DftiDescriptor(final DftiConfigValue precision, final DftiConfigValue forwardDomain, final int[] length)
            throws DftiException {
        if (length.length < 1) {
            throw new IllegalArgumentException();
        }
        long[] handleHolder = new long[1];
        long status = createDescriptor(handleHolder, precision.intValue(), forwardDomain.intValue(), length);
        DftiError.checkStatus(status);
        this.handle = handleHolder[0];
    }

    public void commit() throws DftiException {
        // TODO what happens when commit is called more than once?
        long status = commitDescriptor(handle);
        DftiError.checkStatus(status);
    }

    public void computeBackward(final Buffer inout) throws DftiException {
        long status = computeBackward(handle, inout);
        DftiError.checkStatus(status);
    }

    public void computeBackward(final Buffer in, final Buffer out) throws DftiException {
        long status = computeBackward(handle, in, out);
        DftiError.checkStatus(status);
    }

    // TODO implement copyDescriptor if it could be useful

    public void computeForward(final Buffer inout) throws DftiException {
        long status = computeForward(handle, inout);
        DftiError.checkStatus(status);
    }

    public void computeForward(final Buffer in, final Buffer out) throws DftiException {
        long status = computeForward(handle, in, out);
        DftiError.checkStatus(status);
    }

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
