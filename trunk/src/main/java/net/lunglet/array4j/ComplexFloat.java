package net.lunglet.array4j;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Complex single precision floating point value.
 */
public final class ComplexFloat implements Complex {
    private static final long serialVersionUID = 1L;

    public static final ComplexFloat ZERO = new ComplexFloat(0.0f, 0.0f);

    public static ComplexFloat valueOf(final double real, final double imag) {
        return new ComplexFloat((float) real, (float) imag);
    }

    /** Imaginary component. */
    private final float imag;

    /** Real component. */
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
        if (this == obj) {
            return true;
        }
        final ComplexFloat other = (ComplexFloat) obj;
        return real == other.real && imag == other.imag;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(real).append(imag).toHashCode();
    }

    public float imag() {
        return imag;
    }

    public float real() {
        return real;
    }

    @Override
    public String toString() {
        if (imag >= 0) {
            return real + " + j" + imag;
        } else {
            return real + " - j" + Math.abs(imag);
        }
    }
}
