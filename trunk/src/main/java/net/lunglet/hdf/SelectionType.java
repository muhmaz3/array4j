package net.lunglet.hdf;

public enum SelectionType {
    /** Error. */
    ERROR(-1),

    /** No selection is defined. */
    NONE(0),

    /** A sequence of points is selected. */
    POINTS(1),

    /** A hyperslab or compound hyperslab is selected. */
    HYPERSLABS(2),

    /** The entire dataset is selected. */
    ALL(3);

    private final int value;

    SelectionType(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
