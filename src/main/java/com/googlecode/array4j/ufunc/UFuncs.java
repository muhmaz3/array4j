package com.googlecode.array4j.ufunc;

public final class UFuncs {
    public static final UFunc ADD = new AddUFunc();

    public static final UFunc MULTIPLY = new MultiplyUFunc();

    public static final UFunc SQUARE = new SquareUFunc();

    public static final UFunc SQRT = new SqrtUFunc();

    public static final UFunc LDEXP = new LdExpUFunc();

    public static final UFunc LOG = new LogUFunc();

    public static final UFunc EXP = new ExpUFunc();

    private UFuncs() {
    }
}
