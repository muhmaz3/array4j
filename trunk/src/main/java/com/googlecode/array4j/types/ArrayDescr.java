package com.googlecode.array4j.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.array4j.ByteOrder;

public final class ArrayDescr {
    private static final Map<Types, ArrayDescr> BUILTIN_DESCRS;

    static {
        final Map<Types, ArrayDescr> descrs = new HashMap<Types, ArrayDescr>();
        for (Types type : Types.values()) {
            if (type.getType() != null) {
                descrs.put(type, new ArrayDescr(type));
            }
        }
        BUILTIN_DESCRS = Collections.unmodifiableMap(descrs);
    }

    /*
     * the type object representing an instance of this type -- should not be
     * two type_numbers with the same type object.
     */
    private Object fTypeObj;

    /* kind for this type */
    private final Class<? extends ArrayType> fKind;

    /* unique-character representing this type */
    private final Class<? extends ArrayType> fType;

    /*
     * '>' (big), '<' (little), '|' (not-applicable), or '=' (native).
     */
    private ByteOrder fByteOrder;

    /*
     * non-zero if it has object arrays in fields
     */
    private final int hasobject;

    /* number representing this type */
    private final Types fTypeNum;

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

    private ArrayDescr(final Types type) {
        // TODO figure out if fTypeObj is useful for anything
        fTypeObj = null;
        fKind = type.getKind();
        fType = type.getType();
        fByteOrder = type.getByteOrder();
        // TODO set flags if type is OBJECT
        hasobject = 0;
        fTypeNum = type;
        fElSize = type.getElementSize();
        fAlignment = type.getAlignment();
        fSubArray = null;
        fFields = null;
        f = null;
    }

    public ArrayDescr(final Iterable<Field> fields) {
        this(fields, false);
    }

    // TODO look at <CODE>arraydescr_new</CODE> in NumPy for more stuff
    public ArrayDescr(final Iterable<Field> fields, final boolean align) {
        fTypeObj = null;
        fKind = null;
        fType = null;
        fByteOrder = ByteOrder.NOT_APPLICABLE;
        // TODO set flags if any field contains object types
        hasobject = 0;
        fTypeNum = Types.VOID;
        fElSize = fTypeNum.getElementSize();
        fAlignment = fTypeNum.getAlignment();
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


    public static ArrayDescr valueOf(final Types type) {
        if (BUILTIN_DESCRS.containsKey(type)) {
            return BUILTIN_DESCRS.get(type);
        }
        throw new UnsupportedOperationException("unsupported type");
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
