package com.googlecode.array4j;

public enum Flags {
    EMPTY(0),
    /**
     * Means c-style contiguous (last index varies the fastest). The data
     * elements right after each other.
     */
    CONTIGUOUS(0x0001),
    /**
     * set if array is a contiguous Fortran array: the first index varies
     * the fastest in memory (strides array is reverse of C-contiguous
     * array).
     */
    FORTRAN(0x0002),
    C_CONTIGUOUS(CONTIGUOUS),
    F_CONTIGUOUS(FORTRAN),
    OWNDATA(0x0004),
    FORCECAST(0x0010),
    /**
     * Always copy the array. Returned arrays are always CONTIGUOUS,
     * ALIGNED, and WRITEABLE.
     */
    ENSURECOPY(0x0020),
    ENSUREARRAY(0x0040),
    /**
     * Make sure that the strides are in units of the element size Needed
     * for some operations with record-arrays.
     */
    ELEMENTSTRIDES(0x0080),
    /**
     * Array data is aligned on the appropiate memory address for the type
     * stored according to how the compiler would align things (e.g., an
     * array of integers (4 bytes each) starts on a memory address that's a
     * multiple of 4).
     */
    ALIGNED(0x0100),
    /** Array data has the native endianness. */
    NOTSWAPPED(0x0200),
    /** Array data is writeable. */
    WRITEABLE(0x0400),
    /**
     * If this flag is set, then base contains a pointer to an array of the
     * same size that should be updated with the current contents of this
     * array when this array is deallocated.
     */
    UPDATEIFCOPY(0x1000),
    /** This flag is for the array interface. */
    ARR_HAS_DESCR(0x0800),
    BEHAVED (ALIGNED, WRITEABLE),
    BEHAVED_NS (ALIGNED, WRITEABLE, NOTSWAPPED),
    CARRAY (CONTIGUOUS, BEHAVED),
    CARRAY_RO (CONTIGUOUS, ALIGNED),
    FARRAY (FORTRAN, BEHAVED),
    FARRAY_RO (FORTRAN, ALIGNED),
    DEFAULT(CARRAY),
    IN_ARRAY(CARRAY_RO),
    OUT_ARRAY(CARRAY),
    INOUT_ARRAY(CARRAY, UPDATEIFCOPY),
    IN_FARRAY(FARRAY_RO),
    OUT_FARRAY(FARRAY),
    INOUT_FARRAY (FARRAY, UPDATEIFCOPY),
    UPDATE_ALL (CONTIGUOUS, FORTRAN, ALIGNED);

    private int fValue;

    Flags(final int value) {
        this.fValue = value;
    }

    Flags(final Flags... values) {
        for (final Flags value : values) {
            fValue |= value.fValue;
        }
    }

    public int getValue() {
        return fValue;
    }

    public boolean and(final int value) {
        return (fValue & value) != 0;
    }
}
