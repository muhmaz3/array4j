package net.lunglet.fft.dfti;

public enum DftiConfigValue {
    ALLOW(51),
    BACKWARD_SCRAMBLED(49),
    CCE_FORMAT(57),
    CCS_FORMAT(54),
    COMMITTED(30),
    COMPLEX(32),
    COMPLEX_COMPLEX(39),
    COMPLEX_REAL(40),
    DOUBLE(36),
    INPLACE(43),
    INVALID(-1),
    NONE(53),
    NOT_INPLACE(44),
    ORDERED(48),
    PACK_FORMAT(55),
    PERM_FORMAT(56),
    REAL(33),
    REAL_COMPLEX(41),
    REAL_REAL(42),
    SINGLE(35),
    UNCOMMITTED(31);

    private final int value;

    private DftiConfigValue(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
