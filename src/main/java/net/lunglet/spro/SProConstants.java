package net.lunglet.spro;

public interface SProConstants {
    /** Empty flag. */
    int SPRO_EMPTY_FLAG = 0;

    /** Floor energy below this threshold. */
    double SPRO_ENERGY_FLOOR = 1.0;

    /** Maximum FFT size. */
    int SPRO_MAX_FFT_SIZE = 2048;

    /** Maximum number of filters in a bank. */
    int SPRO_MAX_FILTERS = 100;

    /** Minimum FFT size. */
    int SPRO_MIN_FFT_SIZE = 64;

    /** Mininum number of filters in a bank. */
    int SPRO_MIN_FILTERS = 3;

    int SPRO_STREAM_READ_MODE = 1;

    int SPRO_STREAM_WRITE_MODE = 2;

    /** Delta-delta coefficients. */
    int WITHA = 0x10;

    /** Delta coefficients. */
    int WITHD = 0x08;

    /** Log energy appended. */
    int WITHE = 0x01;

    /** Static energy suppressed. */
    int WITHN = 0x04;

    /** Variance normalization. */
    int WITHR = 0x20;

    /** Mean normalization. */
    int WITHZ = 0x02;
}
