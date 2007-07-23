package net.lunglet.mkl.fft;

import static net.lunglet.mkl.fft.DftiConfigParamType.FLOAT_SCALAR;
import static net.lunglet.mkl.fft.DftiConfigParamType.INT_ARRAY;
import static net.lunglet.mkl.fft.DftiConfigParamType.INT_SCALAR;
import static net.lunglet.mkl.fft.DftiConfigParamType.NAMED_CONSTANT;
import static net.lunglet.mkl.fft.DftiConfigParamType.STRING;

public enum DftiConfigParam {
    FORWARD_DOMAIN(0, NAMED_CONSTANT),
    DIMENSION(1, INT_SCALAR),
    LENGTHS(2, INT_ARRAY),
    PRECISION(3, NAMED_CONSTANT),
    FORWARD_SCALE(4, FLOAT_SCALAR),
    BACKWARD_SCALE(5, FLOAT_SCALAR),
    NUMBER_OF_TRANSFORMS(7, INT_SCALAR),
    COMPLEX_STORAGE(8, NAMED_CONSTANT),
    REAL_STORAGE(9, NAMED_CONSTANT),
    CONJUGATE_EVEN_STORAGE(10, NAMED_CONSTANT),
    PLACEMENT(11, NAMED_CONSTANT),
    INPUT_STRIDES(12, INT_ARRAY),
    OUTPUT_STRIDES(13, INT_ARRAY),
    INPUT_DISTANCE(14, INT_SCALAR),
    OUTPUT_DISTANCE(15, INT_SCALAR),
    ORDERING(18, NAMED_CONSTANT),
    TRANSPOSE(19, NAMED_CONSTANT),
    DESCRIPTOR_NAME(20, STRING),
    PACKED_FORMAT(21, NAMED_CONSTANT),
    COMMIT_STATUS(22, NAMED_CONSTANT),
    VERSION(23, STRING),
    NUMBER_OF_USER_THREADS(26, INT_SCALAR);

    private final int value;

    final DftiConfigParamType type;

    private DftiConfigParam(final int value, final DftiConfigParamType type) {
        this.value = value;
        this.type = type;
    }

    public int intValue() {
        return value;
    }
}
