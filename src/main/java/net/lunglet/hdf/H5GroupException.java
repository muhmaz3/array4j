package net.lunglet.hdf;

public final class H5GroupException extends H5Exception {
    private static final long serialVersionUID = 1L;

    public H5GroupException(final String message) {
        super(message);
    }

    public H5GroupException(final String message, final boolean includeError) {
        super(message, includeError);
    }
}
