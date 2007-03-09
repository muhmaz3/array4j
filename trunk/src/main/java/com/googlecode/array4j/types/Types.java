package com.googlecode.array4j.types;

import com.googlecode.array4j.ByteOrder;

/**
 * Built-in types.
 * <p>
 * This enumeration is analogous to the static array <CODE>_builtin_descrs</CODE>
 * in NumPy.
 *
 * @author albert
 */
public enum Types {
    // TODO kind for BOOL is GENBOOL, whatever that is
    BOOL(null, BooleanType.class, ByteOrder.NOT_APPLICABLE, 1),
    BYTE(SignedIntegerType.class, ByteType.class, ByteOrder.NOT_APPLICABLE, 1),
    UBYTE,
    SHORT(SignedIntegerType.class, ShortType.class, ByteOrder.NATIVE, 2),
    USHORT,
    INT(SignedIntegerType.class, IntegerType.class, ByteOrder.NATIVE, 4),
    UINT,
    LONG(SignedIntegerType.class, LongType.class, ByteOrder.NATIVE, 8),
    ULONG,
    LONGLONG,
    ULONGLONG,
    FLOAT(FloatingType.class, FloatType.class, ByteOrder.NATIVE, 4),
    DOUBLE(FloatingType.class, DoubleType.class, ByteOrder.NATIVE, 8),
    LONGDOUBLE,
    CFLOAT(ComplexFloatingType.class, ComplexFloatType.class, ByteOrder.NATIVE, 2 * 4),
    CDOUBLE(ComplexFloatingType.class, ComplexDoubleType.class, ByteOrder.NATIVE, 2 * 8),
    CLONGDOUBLE,
    OBJECT(ObjectType.class, ObjectType.class, ByteOrder.NOT_APPLICABLE, -1),
    STRING,
    UNICODE,
    VOID(VoidType.class, VoidType.class, ByteOrder.NOT_APPLICABLE, 0),
    NOTYPE,
    /* special flag */
    CHAR,
    // TODO userdef should be have a value of 256
    /* leave room for characters */
    USERDEF;

    private final Class<? extends ArrayType> fKind;

    private final Class<? extends ArrayType> fType;

    private final ByteOrder fByteOrder;

    private final int fElSize;

    private final int fAlignment;

    Types() {
        this(null, null, ByteOrder.NOT_APPLICABLE, -1);
    }

    Types(final Class<? extends ArrayType> kind, final Class<? extends ArrayType> type, final ByteOrder byteOrder,
            final int elsize) {
        if (kind != null && !kind.isAssignableFrom(type)) {
            throw new AssertionError();
        }
        fKind = kind;
        fType = type;
        fByteOrder = byteOrder;
        fElSize = elsize;
        // TODO need to add a native function that calls <CODE>offsetof(struct
        // {char c; type v;}, v)</CODE> for the corresponding C type to figure
        // out the alignment
        fAlignment = -1;
    }

    public Class<? extends ArrayType> getKind() {
        return fKind;
    }

    public Class<? extends ArrayType> getType() {
        return fType;
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
}
