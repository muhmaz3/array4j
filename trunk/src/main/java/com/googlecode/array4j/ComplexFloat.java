package com.googlecode.array4j;

public final class ComplexFloat implements Complex<ComplexFloat> {
    private final float fReal;

    private final float fImag;

    public ComplexFloat(final float real, final float imag) {
        fReal = real;
        fImag = imag;
    }
}
