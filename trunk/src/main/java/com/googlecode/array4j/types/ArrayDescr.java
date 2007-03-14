package com.googlecode.array4j.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.array4j.ByteOrder;
import com.googlecode.array4j.ComplexDouble;
import com.googlecode.array4j.ComplexFloat;
import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.kernel.KernelType;

public final class ArrayDescr {
    private static final Map<Types, ArrayDescr> BUILTIN_DESCRS;

    static {
        final Map<Types, ArrayDescr> descrs = new HashMap<Types, ArrayDescr>();
        for (Types type : Types.values()) {
            if (type.isSupported()) {
                descrs.put(type, new ArrayDescr(type));
            }
        }
        BUILTIN_DESCRS = Collections.unmodifiableMap(descrs);
    }

    /* kind for this type */
    private final ArrayKind fKind;

    /* unique-character representing this type */
    private final Types fType;

    /*
     * '>' (big), '<' (little), '|' (not-applicable), or '=' (native).
     */
    private ByteOrder fByteOrder;

    /*
     * non-zero if it has object arrays in fields
     */
    private final int hasobject;

    /* element size for this type */
    private final int fElSize;

    /* alignment needed for this type */
    private final int fAlignment;

    /*
     * Non-NULL if this type is is an array (C-contiguous) of some other type
     */
    private final Object fSubArray;

    /* The fields dictionary for this type */
    // TODO in numpy, values of this map are a bunch of 2-tuples containing dtypes and something else
    private Map<String, ArrayDescr> fFields;

    /*
     * a table of functions specific for each basic data descriptor
     */
    private final Object[] f;

    private final KernelType fKernelType;

    /**
     * Constructor.
     * <p>
     * This constructor corresponds to the NumPy function <CODE>PyArray_DescrFromType</CODE>.
     */
    private ArrayDescr(final Types type) {
        // TODO figure out if fTypeObj is useful for anything
        fKind = type.getKind();
        fType = type;
        fByteOrder = type.getByteOrder();
        // TODO set flags if type is OBJECT
        hasobject = 0;
        fElSize = type.getElementSize();
        fAlignment = type.getAlignment();
        fSubArray = null;
        fFields = null;
        f = null;
    }

    public ArrayDescr(final Iterable<Field> fields, final boolean align) {
        // TODO look at <CODE>arraydescr_new</CODE> in NumPy for more stuff

        fKind = null;
        fType = Types.VOID;
        fByteOrder = ByteOrder.NOT_APPLICABLE;
        // TODO set flags if any field contains object types
        hasobject = 0;
        fElSize = fType.getElementSize();
        fAlignment = fType.getAlignment();
        fSubArray = null;
        fFields = new LinkedHashMap<String, ArrayDescr>();
        for (Field field : fields) {
            if (fFields.containsKey(field.getName())) {
                throw new IllegalArgumentException("two fields with the same name");
            }
            fFields.put(field.getName(), field.getDescr());
        }
        f = null;
    }


    public static ArrayDescr fromType(final Types type) {
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
            nonNullMintype = fromType(Types.BOOL);
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
            return fromType(Types.DOUBLE);
        } else if (checkComplex(obj)) {
            return fromType(Types.CDOUBLE);
        } else if (checkInt(obj)) {
            if (checkBool(obj)) {
                return fromType(Types.BOOL);
            }
            return fromType(Types.INT);
        } else if (checkLong(obj)) {
            return fromType(Types.LONG);
        }
        // TODO look at supporting Types.OBJECT here
        return null;
    }

    private static ArrayDescr finishType(final ArrayDescr chktype, final ArrayDescr mintype) {
//        outtype = _array_small_type(chktype, minitype);
//        /* VOID Arrays should not occur by "default"
//           unless input was already a VOID */
//        if (outtype->type_num == PyArray_VOID && \
//            minitype->type_num != PyArray_VOID) {
//                Py_DECREF(outtype);
//                return PyArray_DescrFromType(PyArray_OBJECT);
//        }
//        return outtype;
        return null;
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

    public ArrayKind getKind() {
        return fKind;
    }

    public Types getType() {
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

    public Object getSubArray() {
//        return fSubArray;
        return null;
    }

    /**
     * Returns <tt>true</tt> if data type has the native byte order.
     * <p>
     * This method corresponds to the NumPy macro <CODE>PyArray_ISNBO</CODE>.
     */
    public boolean isNativeByteOrder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns <tt>true</tt> if the data types are equivalent.
     * <p>
     * This method corresponds to the NumPy function <CODE>PyArray_EquivTypes</CODE>.
     */
    public boolean isEquivalent(final ArrayDescr typ) {
        if (getElementSize() != typ.getElementSize()) {
            return false;
        }
        if (!getByteOrder().equals(typ.getByteOrder())) {
            return false;
        }
        if (getType().equals(Types.VOID) || typ.getType().equals(Types.VOID)) {
            // TODO check that fields are equivalent
//            return getType().equals(typ.getType()) && false;
            throw new UnsupportedOperationException();
        }
        return getKind().equals(typ.getKind());
    }
}
