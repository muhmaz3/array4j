package com.googlecode.array4j.types;

import java.util.List;
import java.util.Map;

import com.googlecode.array4j.ByteOrder;

public abstract class ArrayDescr<E extends ArrayDescr> {
    /*
     * the type object representing an instance of this type -- should not be
     * two type_numbers with the same type object.
     */
    private Object typeobj;

    /* kind for this type */
    private char kind;

    /* unique-character representing this type */
    private char type;

    /*
     * '>' (big), '<' (little), '|' (not-applicable), or '=' (native).
     */
    private ByteOrder fByteOrder;

    /*
     * non-zero if it has object arrays in fields
     */
    private boolean hasobject;

    /* number representing this type */
    private int typenum;

    /* element size for this type */
    private int elsize;

    /* alignment needed for this type */
    private int alignment;

    /*
     * Non-NULL if this type is is an array (C-contiguous) of some other type
     */
    private Object subarray;

    /* The fields dictionary for this type */
    /*
     * For statically defined descr this is always Py_None
     */
    private Map<String, Object> fields;

    /*
     * An ordered tuple of field names or NULL if no fields are defined
     */
    private List<String> names;

    /*
     * a table of functions specific for each basic data descriptor
     */
    private Object[] f;

    protected ArrayDescr() {
        this(ByteOrder.NATIVE);
    }

    protected ArrayDescr(final ByteOrder byteOrder) {
        this.fByteOrder = byteOrder;
    }

    public abstract E newByteOrder(ByteOrder byteOrder);
}

