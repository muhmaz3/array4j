package com.googlecode.array4j.ufunc;

public final class Error {
    public static final Error DEFAULT_ERROR = new Error();

    private static final int SHIFT_DIVIDEBYZERO = 0;

    private static final int SHIFT_OVERFLOW = 3;

    private static final int SHIFT_UNDERFLOW = 6;

    private static final int SHIFT_INVALID = 9;

    private static final int MIN_BUFSIZE = 2 * 8;

    private static final int MAX_BUFSIZE = MIN_BUFSIZE * 1000000;

    private static final int BUFSIZE = 100000;

    public enum Action {
        IGNORE(0),
        WARN(1),
        THROW(2),
        CALL(3),
        PRINT(4),
        LOG(5);

        private final int fValue;

        Action(final int value) {
            fValue = value;
        }

        public int value() {
            return fValue;
        }
    }

    private final int fBufSize;

    private final Action fDivideByZero;

    private final Action fOverflow;

    private final Action fUnderflow;

    private final Action fInvalid;

    private final ErrorHandler fErrObj;

    private Error() {
        this(BUFSIZE, Action.PRINT, Action.PRINT, Action.IGNORE, Action.PRINT, null);
    }

    public Error(final int bufsize, final Action divideByZero, final Action overflow, final Action underflow,
            final Action invalid, final ErrorHandler errobj) {
        if (bufsize < MIN_BUFSIZE || bufsize > MAX_BUFSIZE || bufsize % 16 != 0) {
            throw new IllegalArgumentException("buffer size (" + bufsize + ") is not in range (" + MIN_BUFSIZE + " - "
                    + MAX_BUFSIZE + ") or not a multiple of 16");
        }
        this.fBufSize = bufsize;
        this.fDivideByZero = divideByZero;
        this.fOverflow = overflow;
        this.fUnderflow = underflow;
        this.fInvalid = invalid;
        this.fErrObj = errobj;
    }

    public int getBufSize() {
        return fBufSize;
    }

    public int getErrorMask() {
        return (fDivideByZero.value() << SHIFT_DIVIDEBYZERO) | (fOverflow.value() << SHIFT_OVERFLOW)
                | (fUnderflow.value() << SHIFT_UNDERFLOW) | (fInvalid.value() << SHIFT_INVALID);
    }

    public ErrorHandler getErrorHandler() {
        return fErrObj;
    }
}
