package com.googlecode.array4j.types;

public enum ScalarKinds {
    NOSCALAR(-1),
    BOOL(0),
    INTPOS(1),
    INTNEG(2),
    FLOAT(3),
    COMPLEX(4),
    OBJECT(5);

    private int fValue;

    ScalarKinds(final int value) {
        this.fValue = value;
    }

    public int getValue() {
        return fValue;
    }
}
