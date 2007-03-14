package com.googlecode.array4j;


// TODO rename Types to ArrayType

/**
 * Built-in types.
 * <p>
 * This enumeration is analogous to the static array <CODE>_builtin_descrs</CODE>
 * in NumPy.
 *
 * @author albert
 */
public enum ArrayType {
    // TODO kind for BOOL is GENBOOL, whatever that is
    BOOL(ArrayKind.GENBOOL, ByteOrder.NOT_APPLICABLE, 1),
    BYTE(ArrayKind.SIGNED, ByteOrder.NOT_APPLICABLE, 1),
    UBYTE,
    SHORT(ArrayKind.SIGNED, ByteOrder.NATIVE, 2),
    USHORT,
    INT(ArrayKind.SIGNED, ByteOrder.NATIVE, 4),
    UINT,
    LONG(ArrayKind.SIGNED, ByteOrder.NATIVE, 8),
    ULONG,
    LONGLONG,
    ULONGLONG,
    FLOAT(ArrayKind.FLOATING, ByteOrder.NATIVE, 4),
    DOUBLE(ArrayKind.FLOATING, ByteOrder.NATIVE, 8),
    LONGDOUBLE,
    CFLOAT(ArrayKind.COMPLEX, ByteOrder.NATIVE, 2 * 4),
    CDOUBLE(ArrayKind.COMPLEX, ByteOrder.NATIVE, 2 * 8),
    CLONGDOUBLE,
    OBJECT(null, ByteOrder.NOT_APPLICABLE, -1),
    STRING,
    UNICODE,
    VOID(null, ByteOrder.NOT_APPLICABLE, 0),
    /* special flag */
    NOTYPE,
    CHAR,
    // TODO userdef should be have a value of 256
    /* leave room for characters */
    USERDEF;

    private final ArrayKind fKind;

    private final ByteOrder fByteOrder;

    private final int fElSize;

    private final int fAlignment;

    ArrayType() {
        this(null, ByteOrder.NOT_APPLICABLE, -1);
    }

    ArrayType(final ArrayKind kind, final ByteOrder byteOrder, final int elsize) {
        if (kind != null && !kind.isAssignableFrom(this)) {
            throw new AssertionError();
        }
        fKind = kind;
        fByteOrder = byteOrder;
        fElSize = elsize;
        // TODO need to add a native function that calls <CODE>offsetof(struct
        // {char c; type v;}, v)</CODE> for the corresponding C type to figure
        // out the alignment
        fAlignment = -1;
    }

    public ArrayKind getKind() {
        return fKind;
    }

    public ByteOrder getByteOrder() {
        return fByteOrder;
    }

    public int getElementSize() {
        return fElSize;
    }

    public int getAlignment() {
        return fAlignment;
    }

    public boolean isSupported() {
        return fKind != null;
    }
}
