package net.lunglet.mkl.fft;

// TODO possibly make this a runtime exception

public class DftiException extends Exception {
    private static final long serialVersionUID = 1L;

    public DftiException(final String message) {
        super(message);
    }
}
