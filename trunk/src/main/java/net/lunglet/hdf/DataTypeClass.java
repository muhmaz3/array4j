package net.lunglet.hdf;

public enum DataTypeClass {
    NO_CLASS(-1),
    INTEGER(0),
    FLOAT(1),
    TIME(2),
    STRING(3),
    BITFIELD(4),
    OPAQUE(5),
    COMPOUND(6),
    REFERENCE(7),
    ENUM(8),
    VLEN(9),
    ARRAY(10);

    private final int value;

    DataTypeClass(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
