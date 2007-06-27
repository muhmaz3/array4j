package com.googlecode.array4j;

public final class ComplexFloat implements Complex<ComplexFloat> {
    private final float real;

    private final float imag;

    public ComplexFloat() {
        this(0.0f, 0.0f);
    }

    public ComplexFloat(final float real, final float imag) {
        this.real = real;
        this.imag = imag;
    }

    public float real() {
        return real;
    }

    public float imag() {
        return imag;
    }

    @Override
    public String toString() {
        return real + " + j" + imag;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof ComplexFloat)) {
            return false;
        }
        ComplexFloat other = (ComplexFloat) obj;
        return real == other.real && imag == other.imag;
    }
}
