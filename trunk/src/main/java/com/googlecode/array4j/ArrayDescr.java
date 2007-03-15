package com.googlecode.array4j;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.array4j.kernel.Interface;
import com.googlecode.array4j.kernel.KernelType;

public final class ArrayDescr {
    private static final Map<ArrayType, ArrayDescr> BUILTIN_DESCRS;

    static {
        final Map<ArrayType, ArrayDescr> descrs = new HashMap<ArrayType, ArrayDescr>();
        for (ArrayType type : ArrayType.values()) {
            descrs.put(type, new ArrayDescr(type, KernelType.DEFAULT));
        }
        BUILTIN_DESCRS = Collections.unmodifiableMap(descrs);
    }

    /* kind for this type */
    private final ArrayKind fKind;

    /* unique-character representing this type */
    private final ArrayType fType;

    /*
     * '>' (big), '<' (little), '|' (not-applicable), or '=' (native).
     */
    private ByteOrder fByteOrder;

    /*
     * non-zero if it has object arrays in fields
     */
    private final int hasobject;

    /* element size for this type */
    private int fItemSize;

    /* alignment needed for this type */
    private final int fAlignment;

    /*
     * Non-NULL if this type is is an array (C-contiguous) of some other type
     */
    private final Object fSubArray;

    /* The fields dictionary for this type */
    // TODO in numpy, values of this map are a bunch of 2-tuples containing dtypes and something else
    private Map<String, ArrayDescr> fFields;

    private final KernelType fKernelType;

    /*
     * a table of functions specific for each basic data descriptor
     */
    private final ArrayFunctions fArrFuncs;

    /**
     * Constructor.
     * <p>
     * This constructor corresponds to the NumPy function <CODE>PyArray_DescrFromType</CODE>.
     */
    private ArrayDescr(final ArrayType type, final KernelType kernelType) {
        // TODO might want to fix byte order as little or big endian at
        // construction so that we don't have NATIVE and NOT_APPLICABLE byte
        // orders floating around

        // TODO figure out if fTypeObj is useful for anything
        fKind = type.getKind();
        fType = type;
        fByteOrder = type.getByteOrder();
        // TODO set flags if type is OBJECT
        hasobject = 0;
        fItemSize = type.getElementSize();
        fAlignment = type.getAlignment();
        fSubArray = null;
        fFields = null;
        fKernelType = kernelType;
        fArrFuncs = type.getArrayFunctions(kernelType);
    }

    /**
     * This constructor corresponds to NumPy function <CODE>PyArray_DescrNew</CODE>.
     */
    public ArrayDescr(final ArrayDescr base) {
        fKind = base.fKind;
        fType = base.fType;
        fByteOrder = base.fByteOrder;
        hasobject = base.hasobject;
        fItemSize = base.fItemSize;
        fAlignment = base.fAlignment;
        // TODO fields
        fKernelType = base.fKernelType;
        fArrFuncs = base.fArrFuncs;
//        if (base.fSubArray != null) {
//        }
        fSubArray = null;
    }

    public ArrayDescr(final Iterable<Field> fields, final boolean align) {
        this(fields, align, KernelType.DEFAULT);
    }

    public ArrayDescr(final Iterable<Field> fields, final boolean align, final KernelType kernelType) {
        // TODO look at <CODE>arraydescr_new</CODE> in NumPy for more stuff that
        // has to be done for record arrays
        fKind = null;
        fType = ArrayType.VOID;
        fByteOrder = ByteOrder.NOT_APPLICABLE;
        // TODO set flags if any field contains object types
        hasobject = 0;
        fItemSize = fType.getElementSize();
        fAlignment = fType.getAlignment();
        fSubArray = null;
        fFields = new LinkedHashMap<String, ArrayDescr>();
        for (Field field : fields) {
            if (fFields.containsKey(field.getName())) {
                throw new IllegalArgumentException("two fields with the same name");
            }
            fFields.put(field.getName(), field.getDescr());
        }
        fArrFuncs = null;
        fKernelType = kernelType;
    }


    public static ArrayDescr fromType(final ArrayType type) {
        if (BUILTIN_DESCRS.containsKey(type)) {
            return BUILTIN_DESCRS.get(type);
        }
        throw new UnsupportedOperationException("unsupported type");
    }

    public static ArrayDescr fromObject(final Object obj) {
        return fromObject(obj, null);
    }

    /**
     * This code corresponds to the NumPy function <CODE>PyArray_DescrFromObject</CODE>.
     */
    public static ArrayDescr fromObject(final Object obj, final ArrayDescr mintype) {
        return findType(obj, mintype, 32);
    }

    private static ArrayDescr findType(final Object obj, final ArrayDescr mintype, final int max) {
        ArrayDescr chktype;
        if (checkArray(obj)) {
            chktype = ((DenseArray) obj).dtype();
            if (mintype == null) {
                return chktype;
            }
            return finishType(chktype, mintype);
        } else if (isScalar(obj)) {
            chktype = fromScalar(obj);
            if (mintype == null) {
                return chktype;
            }
            return finishType(chktype, mintype);
        }

        final ArrayDescr nonNullMintype;
        if (mintype == null) {
            nonNullMintype = fromType(ArrayType.BOOL);
        } else {
            nonNullMintype = mintype;
        }

        if (max >= 0) {
            chktype = findScalarType(obj);

            // TODO NumPy does various other checks on obj here

            if (chktype == null) {
                chktype = useDefaultType(obj);
            }
        } else {
            chktype = useDefaultType(obj);
        }

        return finishType(chktype, nonNullMintype);
    }

    private static ArrayDescr useDefaultType(final Object obj) {
        throw new UnsupportedOperationException();
    }

    private static ArrayDescr findScalarType(final Object obj) {
        if (checkFloat(obj)) {
            return fromType(ArrayType.DOUBLE);
        } else if (checkComplex(obj)) {
            return fromType(ArrayType.CDOUBLE);
        } else if (checkInt(obj)) {
            if (checkBool(obj)) {
                return fromType(ArrayType.BOOL);
            }
            return fromType(ArrayType.INT);
        } else if (checkLong(obj)) {
            return fromType(ArrayType.LONG);
        }
        // TODO look at supporting Types.OBJECT here
        return null;
    }

    private static ArrayDescr finishType(final ArrayDescr chktype, final ArrayDescr mintype) {
        final ArrayDescr outtype = smallType(chktype, mintype);
        /* VOID Arrays should not occur by "default" unless input was already a VOID */
        if (outtype.type().equals(ArrayType.VOID) && !mintype.type().equals(ArrayType.VOID)) {
            return fromType(ArrayType.OBJECT);
        }
        return outtype;
    }

    /**
     * This code corresponds to the NumPy function <CODE>_array_small_type</CODE>.
     */
    private static ArrayDescr smallType(final ArrayDescr chktype, final ArrayDescr mintype) {
        if (chktype.isEquivalent(mintype)) {
            return mintype;
        }

        ArrayDescr outtype;
        if (chktype.type().compareTo(mintype.type()) > 0) {
            outtype = fromType(chktype.type());
        } else {
            outtype = fromType(mintype.type());
        }
        for (ArrayType typ : ArrayType.values()) {
            if (typ.canCastSafely(chktype.type()) && typ.canCastSafely(mintype.type())) {
                outtype = fromType(typ);
                break;
            }
        }

        // TODO handle extended types
        if (false) {
        }

        return outtype;
    }

    private static boolean checkArray(final Object obj) {
        return obj instanceof DenseArray;
    }

    private static boolean isScalar(final Object obj) {
        return checkFloat(obj) || checkLong(obj) || checkComplex(obj);
    }

    private static boolean checkFloat(final Object obj) {
        return obj instanceof Double || obj instanceof Float;
    }

    private static boolean checkComplex(final Object obj) {
        return obj instanceof ComplexDouble || obj instanceof ComplexFloat;
    }

    private static boolean checkBool(final Object obj) {
        return obj instanceof Boolean;
    }

    private static boolean checkInt(final Object obj) {
        return obj instanceof Integer || obj instanceof Byte || checkBool(obj);
    }

    private static boolean checkLong(final Object obj) {
        return obj instanceof Long || checkInt(obj);
    }

    /**
     * This code corresponds to the NumPy function <CODE>PyArray_DescrFromScalar</CODE>.
     */
    public static ArrayDescr fromScalar(final Object obj) {
        return null;
    }

    public ArrayKind kind() {
        return fKind;
    }

    public ArrayType type() {
        return fType;
    }

    public ByteOrder byteOrder() {
        return fByteOrder;
    }

    public int itemSize() {
        return fItemSize;
    }

    void setItemSize(final int itemsize) {
        this.fItemSize = itemsize;
    }

    public int alignment() {
        return fAlignment;
    }

    public ArrayFunctions getArrayFunctions() {
        return fArrFuncs;
    }

    public Object getSubArray() {
        // TODO need to implement a few bits in DenseArray constructor before
        // this can be enabled
//        return fSubArray;
        return null;
    }

    /**
     * Returns <tt>true</tt> if data type has the native byte order.
     * <p>
     * This method corresponds to the NumPy macro <CODE>PyArray_ISNBO</CODE>.
     */
    public boolean isNativeByteOrder() {
        if (fByteOrder.equals(ByteOrder.NATIVE) || fByteOrder.equals(ByteOrder.NOT_APPLICABLE)) {
            return true;
        }
        if (java.nio.ByteOrder.nativeOrder().equals(java.nio.ByteOrder.LITTLE_ENDIAN)) {
            return fByteOrder.equals(ByteOrder.LITTLE_ENDIAN);
        } else {
            return fByteOrder.equals(ByteOrder.BIG_ENDIAN);
        }
    }

    /**
     * Returns <tt>true</tt> if the data types are equivalent.
     * <p>
     * This method corresponds to the NumPy function <CODE>PyArray_EquivTypes</CODE>.
     */
    public boolean isEquivalent(final ArrayDescr typ) {
        if (itemSize() != typ.itemSize()) {
            return false;
        }
        if (!byteOrder().equals(typ.byteOrder())) {
            return false;
        }
        if (type().equals(ArrayType.VOID) || typ.type().equals(ArrayType.VOID)) {
            // TODO check that fields are equivalent
            return type().equals(typ.type()) && false;
        }
        return kind().equals(typ.kind());
    }

    public ByteBuffer createBuffer(final int capacity) {
        return Interface.kernel(fKernelType).createBuffer(capacity);
    }

    /**
     * This method corresponds to the NumPy function <CODE>PyArray_CanCastTo</CODE>.
     */
    public boolean canCastTo(final ArrayDescr to) {
        final ArrayType fromtype = type();
        final ArrayType totype = to.type();
        // TODO Check String and Unicode more closely
        return fromtype.canCastSafely(totype);
    }
}
