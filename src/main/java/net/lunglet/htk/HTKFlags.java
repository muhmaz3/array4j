package net.lunglet.htk;

public interface HTKFlags {
    /** Has acceleration coefficients. */
    int HAS_ACCELERATION = 0x200;

    /** Has 0th cepstral coefficient. */
    int HAS_C0 = 0x2000;

    /** Has delta coefficients. */
    int HAS_DELTA = 0x100;

    /** Has energy. */
    int HAS_ENERGY = 0x40;

    /** Suppress absolute energy. */
    int SUPPRESS_ABSOLUTE_ENERGY = 0x80;

    /** Zero mean static coefficients. */
    int ZERO_MEAN = 0x800;
}
