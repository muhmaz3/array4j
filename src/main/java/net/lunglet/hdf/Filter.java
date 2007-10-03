package net.lunglet.hdf;

public enum Filter {
    /* deflation like gzip */
    DEFLATE(1),
    /* fletcher32 checksum of EDC */
    FLETCHER32(3),
    /* shuffle the data */
    SHUFFLE(2),
    /* szip compression */
    SZIP(4);

    private final int value;

    Filter(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

}
