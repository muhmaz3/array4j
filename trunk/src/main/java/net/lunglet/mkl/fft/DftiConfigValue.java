package net.lunglet.mkl.fft;

public enum DftiConfigValue {
    INVALID(-1),
    COMMITTED(30),
    UNCOMMITTED(31),
    COMPLEX(32),
    REAL(33),
    SINGLE(35),
    DOUBLE(36),
    COMPLEX_COMPLEX(39),
    COMPLEX_REAL(40),
    REAL_COMPLEX(41),
    REAL_REAL(42),
    INPLACE(43),
    NOT_INPLACE(44),
    ORDERED(48),
    BACKWARD_SCRAMBLED(49),
    ALLOW(51),
    NONE(53),
    CCS_FORMAT(54),
    PACK_FORMAT(55),
    PERM_FORMAT(56),
    CCE_FORMAT(57);

    private final int value;

    private DftiConfigValue(final int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
