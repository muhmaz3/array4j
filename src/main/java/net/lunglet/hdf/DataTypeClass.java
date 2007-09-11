package net.lunglet.hdf;

public enum DataTypeClass {
    ARRAY(10),
    BITFIELD(4),
    COMPOUND(6),
    ENUM(8),
    FLOAT(1),
    INTEGER(0),
    OPAQUE(5),
    REFERENCE(7),
    STRING(3),
    TIME(2),
    VLEN(9);

    private final int value;

    DataTypeClass(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
