package net.lunglet.hdf;

public enum SelectionOperator {
    /**
     * Retains only the overlapping portions of the new selection and the
     * existing selection.
     */
    AND(2),

    /** Append elements to end of point selection. */
    APPEND(6),

    NOOP(-1),

    /**
     * Retains only elements of the new selection that are not in the existing
     * selection.
     */
    NOTA(5),

    /**
     * Retains only elements of the existing selection that are not in the new
     * selection.
     */
    NOTB(4),

    /**
     * Adds the new selection to the existing selection.
     */
    OR(1),

    /** Prepend elements to beginning of point selection. */
    PREPEND(7),

    /**
     * Replaces the existing selection with the parameters from this call.
     * <p>
     * Overlapping blocks are not supported with this operator.
     */
    SET(0),

    /**
     * Retains only the elements that are members of the new selection or the
     * existing selection, excluding elements that are members of both
     * selections.
     */
    XOR(3);

    private final int value;

    SelectionOperator(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
