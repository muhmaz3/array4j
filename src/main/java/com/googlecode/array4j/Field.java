package com.googlecode.array4j;

public final class Field {
    private final String fName;

    private final ArrayDescr fDescr;

    private final int[] fShape;

    public Field(final String name, final ArrayDescr dtype) {
        this(name, dtype, 0);
    }

    public Field(final String name, final ArrayDescr dtype, final int... shape) {
        fName = name;
        fDescr = dtype;
        fShape = new int[shape.length];
        System.arraycopy(shape, 0, fShape, 0, shape.length);
    }

    public String getName() {
        return fName;
    }

    public ArrayDescr getDescr() {
        return fDescr;
    }
}
