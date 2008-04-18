package net.lunglet.htk;

public enum HTKDataType {
    /** Acoustic waveform. */
    WAVEFORM,
    /** Linear prediction coefficients. */
    LPC,
    /** LPC Reflection coefficients. */
    LPREFC,
    /** LPC Cepstral coefficients. */
    LPCEPSTRA,
    /** LPC cepstral+delta coefficients (obsolete). */
    LPDELCEP,
    /** LPC Reflection coefficients (16 bit fixed point). */
    IREFC,
    /** Mel frequency cepstral coefficients. */
    MFCC,
    /** Log Fliter bank energies. */
    FBANK,
    /** Linear Mel-scaled spectrum. */
    MELSPEC,
    /** User defined features. */
    USER,
    /** Vector quantised codebook. */
    DISCRETE,
    /** Perceptual Linear prediction. */
    PLP;

    private HTKDataType() {
    }
}
