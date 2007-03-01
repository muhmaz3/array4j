package com.googlecode.array4j.types;

//TODO combine typechars and typenums into one enum

public enum Types {
    BOOL,
    BYTE,
    UBYTE,
    SHORT,
    USHORT,
    INT,
    UINT,
    LONG,
    ULONG,
    LONGLONG,
    ULONGLONG,
    FLOAT,
    DOUBLE,
    LONGDOUBLE,
    CFLOAT,
    CDOUBLE,
    CLONGDOUBLE,
    OBJECT,
    STRING,
    UNICODE,
    VOID,
    NTYPES,
    NOTYPE,
    /* special flag */
    CHAR,
    // TODO userdef should be have a value of 256
    /* leave room for characters */
    USERDEF;
}
