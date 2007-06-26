package com.googlecode.array4j;

import java.nio.ByteBuffer;

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
            return Interface.kernel(kernelType).getIntegerFunctions();
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
            return Interface.kernel(kernelType).getDoubleFunctions();
        }
    },
    CFLOAT(ArrayKind.COMPLEX, ByteOrder.NATIVE, 2 * 4) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return null;
        }
    },
    CDOUBLE(ArrayKind.COMPLEX, ByteOrder.NATIVE, 2 * 8) {
        public ArrayFunctions getArrayFunctions(final KernelType kernelType) {
            return Interface.kernel(kernelType).getComplexDoubleFunctions();
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

    public boolean isBool() {
        return equals(BOOL);
    }

    public boolean isInteger() {
        return compareTo(BYTE) >= 0 && compareTo(LONG) <= 0;
    }

    public boolean isFloat() {
        return equals(DOUBLE) || equals(FLOAT);
    }

    public boolean isSigned() {
        return equals(BYTE) || equals(SHORT) || equals(INT) || equals(LONG);
    }

    public boolean isUnsigned() {
        return false;
    }

    public boolean isComplex() {
        return equals(CDOUBLE) || equals(CFLOAT);
    }

    public boolean isFlexible() {
        return compareTo(OBJECT) >= 0 && compareTo(VOID) <= 0;
    }

    public boolean isObject() {
        return equals(OBJECT);
    }

    /**
     * This method corresponds to the NumPy macro <CODE>PyTypeNum_ISNUMBER</CODE>.
     */
    public boolean isNumber() {
        return compareTo(DOUBLE) <= 0;
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
        final int telsize = to.itemSize();
        final int felsize = from.itemSize();

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
                return (telsize >> 1) >= felsize;
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

    /**
     * This code corresponds to the NumPy function <CODE>PyArray_CanCoerceScalar</CODE>.
     */
    public boolean canCoerceScalar(final ArrayType neededtype, final ScalarKind scalar) {
        if (scalar.equals(ScalarKind.NOSCALAR)) {
            return canCastSafely(neededtype);
        }
        final ArrayDescr from = ArrayDescr.fromType(this);
        if (false) {
            // TODO check cast function or something
        }
        switch (scalar) {
        case BOOL:
        case OBJECT:
            return canCastSafely(neededtype);
        default:
            // TODO support user defined types?
            switch (scalar) {
            case INTPOS:
                return neededtype.compareTo(BYTE) >= 0;
            case INTNEG:
                return neededtype.compareTo(BYTE) >= 0 && !neededtype.isUnsigned();
            case FLOAT:
                return neededtype.compareTo(FLOAT) >= 0;
            case COMPLEX:
                return neededtype.compareTo(CFLOAT) >= 0;
            default:
                /* should never get here... */
                throw new AssertionError();
            }
        }
    }

    public int getElementsInBuffer(final ByteBuffer data) {
        if (data.capacity() % fElSize != 0) {
            throw new IllegalArgumentException("buffer contains incomplete elements");
        }
        return data.capacity() / fElSize;
    }
}
