package net.lunglet.fft.dfti;

public enum DftiError {
    DFTI_1D_LENGTH_EXCEEDS_INT32(9L),
    DFTI_BAD_DESCRIPTOR(5L),
    DFTI_INCONSISTENT_CONFIGURATION(3L),
    DFTI_INVALID_CONFIGURATION(2L),
    DFTI_MEMORY_ERROR(1L),
    DFTI_MKL_INTERNAL_ERROR(7L),
    DFTI_MULTITHREADED_ERROR(4L),
    DFTI_NO_ERROR(0L),
    DFTI_NUMBER_OF_THREADS_ERROR(8L),
    DFTI_UNIMPLEMENTED(6L);

    static {
        // TODO try to detect from MKL DLL using MKLGetVersion
        System.loadLibrary("array4j");
    }

    public static void checkStatus(final long status) throws DftiException {
        if (!errorClass(status, DFTI_NO_ERROR.value)) {
            throw new DftiException(errorMessage(status));
        }
    }

    private static native boolean errorClass(long status, long errorClass);

    // TODO make sure this doesn't leak
    private static native String errorMessage(long status);

    private final long value;

    private DftiError(final long value) {
        this.value = value;
    }

    public long longValue() {
        return value;
    }
}
