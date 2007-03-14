package com.googlecode.array4j;

public enum ScalarKind {
    NOSCALAR(-1),
    BOOL(0),
    INTPOS(1),
    INTNEG(2),
    FLOAT(3),
    COMPLEX(4),
    OBJECT(5);

    private int fValue;

    ScalarKind(final int value) {
        this.fValue = value;
    }

    public int getValue() {
        return fValue;
    }
}
