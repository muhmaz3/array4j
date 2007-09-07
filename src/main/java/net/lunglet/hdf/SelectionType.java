package net.lunglet.hdf;

public enum SelectionType {
    /** The entire dataset is selected. */
    ALL(3),

    /** Error. */
    ERROR(-1),

    /** A hyperslab or compound hyperslab is selected. */
    HYPERSLABS(2),

    /** No selection is defined. */
    NONE(0),

    /** A sequence of points is selected. */
    POINTS(1);

    private final int value;

    SelectionType(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
