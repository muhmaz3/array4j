package com.googlecode.array4j;

public enum Storage {
    DIRECT, JAVA;

    public static final Storage DEFAULT_FOR_DENSE = JAVA;

    public static final Storage DEFAULT_FOR_SPARSE = null;
}
