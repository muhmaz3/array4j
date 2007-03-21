package com.googlecode.array4j.ufunc;

public final class UFuncs {
    public static final UFunc ADD = new AddUFunc();

    public static final UFunc MULTIPLY = new MultiplyUFunc();

    public static final UFunc SQUARE = new SquareUFunc();

    private UFuncs() {
    }
}
