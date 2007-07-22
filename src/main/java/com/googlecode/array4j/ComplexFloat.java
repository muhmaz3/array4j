package com.googlecode.array4j;

public final class ComplexFloat implements Complex<ComplexFloat> {
    public static ComplexFloat valueOf(final double real, final double imag) {
        return new ComplexFloat((float) real, (float) imag);
    }

    public static ComplexFloat valueOf(final float real, final float imag) {
        return new ComplexFloat(real, imag);
    }

    public static ComplexFloat valueOf(final long real, final long imag) {
        return new ComplexFloat(real, imag);
    }

    private final float imag;

    private final float real;

    public ComplexFloat() {
        this(0.0f, 0.0f);
    }

    public ComplexFloat(final float real, final float imag) {
        this.real = real;
        this.imag = imag;
    }

    public ComplexFloat conj() {
        return new ComplexFloat(real, -imag);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof ComplexFloat)) {
            return false;
        }
        final ComplexFloat other = (ComplexFloat) obj;
        return real == other.real && imag == other.imag;
    }

    public float imag() {
        return imag;
    }

    public float real() {
        return real;
    }

    @Override
    public String toString() {
        return real + " + j" + imag;
    }
}
