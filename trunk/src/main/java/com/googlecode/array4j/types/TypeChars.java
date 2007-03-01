package com.googlecode.array4j.types;

public enum TypeChars {
    BOOLLTR('?'),
    BYTELTR('b'),
    UBYTELTR('B'),
    SHORTLTR('h'),
    USHORTLTR('H'),
    INTLTR('i'),
    UINTLTR('I'),
    LONGLTR('l'),
    ULONGLTR('L'),
    LONGLONGLTR('q'),
    ULONGLONGLTR('Q'),
    FLOATLTR('f'),
    DOUBLELTR('d'),
    LONGDOUBLELTR('g'),
    CFLOATLTR('F'),
    CDOUBLELTR('D'),
    CLONGDOUBLELTR('G'),
    OBJECTLTR('O'),
    STRINGLTR('S'),
    STRINGLTR2('a'),
    UNICODELTR('U'),
    VOIDLTR('V'),
    CHARLTR('c'),

    /*
     * No Descriptor), just a define -- this let's Python users specify an array
     * of integers large enough to hold a pointer on the platform
     */
    INTPLTR('p'),
    UINTPLTR('P'),

    GENBOOLLTR('b'),
    SIGNEDLTR('i'),
    UNSIGNEDLTR('u'),
    FLOATINGLTR('f'),
    COMPLEXLTR('c');

    private char fValue;

    TypeChars(final char value) {
        this.fValue = value;
    }

    public char getValue() {
        return fValue;
    }
}
