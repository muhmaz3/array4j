package com.googlecode.array4j;

import com.googlecode.array4j.kernel.Interface;
import com.googlecode.array4j.kernel.KernelType;

/**
 * Built-in types.
 * <p>
 * This enumeration is analogous to the static array <CODE>_builtin_descrs</CODE>
 * in NumPy.
 *
 * @author albert
 */
public enum ArrayType {
    BOOL(ArrayKind.GENBOOL, ByteOrder.NOT_APPLICABLE, 1) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    BYTE(ArrayKind.SIGNED, ByteOrder.NOT_APPLICABLE, 1) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    SHORT(ArrayKind.SIGNED, ByteOrder.NATIVE, 2) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    INT(ArrayKind.SIGNED, ByteOrder.NATIVE, 4) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    LONG(ArrayKind.SIGNED, ByteOrder.NATIVE, 8) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    FLOAT(ArrayKind.FLOATING, ByteOrder.NATIVE, 4) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    DOUBLE(ArrayKind.FLOATING, ByteOrder.NATIVE, 8) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return Interface.kernel(kernelType).getDoubleArrayFunctions();
        }
    },
    CFLOAT(ArrayKind.COMPLEX, ByteOrder.NATIVE, 2 * 4) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    CDOUBLE(ArrayKind.COMPLEX, ByteOrder.NATIVE, 2 * 8) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    OBJECT(null, ByteOrder.NOT_APPLICABLE, -1) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    VOID(null, ByteOrder.NOT_APPLICABLE, 0) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    };

    private final ArrayKind fKind;

    private final ByteOrder fByteOrder;

    private final int fElSize;

    private final int fAlignment;

    ArrayType(final ArrayKind kind, final ByteOrder byteOrder, final int elsize) {
        // TODO reenable this check at some point
        // if (kind != null && !kind.isAssignableFrom(this)) {
        // throw new AssertionError();
        // }
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

    public abstract ArrayFunctions getArrayFunctions(final KernelType kernelType);

    public boolean isInteger() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    public boolean isUnsigned() {
        return false;
    }

    public boolean isComplex() {
        return false;
    }

    /**
     * This code corresponds to the NumPy function <CODE>PyArray_CanCastSafely</CODE>.
     */
    public boolean canCastSafely(final ArrayType totype) {
        final ArrayType fromtype = this;
        if (fromtype.equals(totype)) {
            return true;
        }
        if (fromtype.equals(BOOL)) {
            return true;
        }
        if (totype.equals(BOOL)) {
            return false;
        }
        if (totype.equals(OBJECT) || totype.equals(VOID)) {
            return true;
        }
        if (fromtype.equals(OBJECT) || fromtype.equals(VOID)) {
            return true;
        }

        final ArrayDescr from = ArrayDescr.fromType(fromtype);

        // TODO cancastto function stuff

        final ArrayDescr to = ArrayDescr.fromType(totype);
        final int telsize = to.getElementSize();
        final int felsize = from.getElementSize();

        switch (fromtype) {
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
            if (totype.isInteger()) {
                if (totype.isUnsigned()) {
                    return false;
                } else {
                    return telsize >= felsize;
                }
            } else if (totype.isFloat()) {
                if (felsize < 8) {
                    return telsize > felsize;
                } else {
                    return telsize >= felsize;
                }
            } else if (totype.isComplex()) {
                if (felsize < 8) {
                    return (telsize >> 1) > felsize;
                } else {
                    return (telsize >> 1) >= felsize;
                }
            } else {
                return totype.compareTo(fromtype) > 0;
            }
        case FLOAT:
        case DOUBLE:
            if (totype.isComplex()) {
                return (telsize >> 1) > felsize;
            } else {
                return totype.compareTo(fromtype) > 0;
            }
        case CFLOAT:
        case CDOUBLE:
            return totype.compareTo(fromtype) > 0;
        default:
            return false;
        }
    }
}
