package net.lunglet.fft.dfti;

import static net.lunglet.fft.dfti.DftiConfigParamType.FLOAT_SCALAR;
import static net.lunglet.fft.dfti.DftiConfigParamType.INT_ARRAY;
import static net.lunglet.fft.dfti.DftiConfigParamType.INT_SCALAR;
import static net.lunglet.fft.dfti.DftiConfigParamType.NAMED_CONSTANT;
import static net.lunglet.fft.dfti.DftiConfigParamType.STRING;

public enum DftiConfigParam {
    BACKWARD_SCALE(5, FLOAT_SCALAR),
    COMMIT_STATUS(22, NAMED_CONSTANT),
    COMPLEX_STORAGE(8, NAMED_CONSTANT),
    CONJUGATE_EVEN_STORAGE(10, NAMED_CONSTANT),
    DESCRIPTOR_NAME(20, STRING),
    DIMENSION(1, INT_SCALAR),
    FORWARD_DOMAIN(0, NAMED_CONSTANT),
    FORWARD_SCALE(4, FLOAT_SCALAR),
    INPUT_DISTANCE(14, INT_SCALAR),
    INPUT_STRIDES(12, INT_ARRAY),
    LENGTHS(2, INT_ARRAY),
    NUMBER_OF_TRANSFORMS(7, INT_SCALAR),
    NUMBER_OF_USER_THREADS(26, INT_SCALAR),
    ORDERING(18, NAMED_CONSTANT),
    OUTPUT_DISTANCE(15, INT_SCALAR),
    OUTPUT_STRIDES(13, INT_ARRAY),
    PACKED_FORMAT(21, NAMED_CONSTANT),
    PLACEMENT(11, NAMED_CONSTANT),
    PRECISION(3, NAMED_CONSTANT),
    REAL_STORAGE(9, NAMED_CONSTANT),
    TRANSPOSE(19, NAMED_CONSTANT),
    VERSION(23, STRING);

    final DftiConfigParamType type;

    private final int value;

    private DftiConfigParam(final int value, final DftiConfigParamType type) {
        this.value = value;
        this.type = type;
    }

    public int intValue() {
        return value;
    }
}
