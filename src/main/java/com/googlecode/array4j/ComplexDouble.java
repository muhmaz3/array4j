package com.googlecode.array4j;

public final class ComplexDouble implements Complex<ComplexDouble> {
    private final double fReal;

    private final double fImag;

    public ComplexDouble(final double real, final double imag) {
        fReal = real;
        fImag = imag;
    }
}
