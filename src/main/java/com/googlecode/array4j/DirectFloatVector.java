package com.googlecode.array4j;

import java.nio.DoubleBuffer;

public final class DirectFloatVector extends AbstractDenseVector<DirectFloatVector> implements
        FloatVector<DirectFloatVector>, DenseVector<DirectFloatVector> {
    private final DoubleBuffer data;

}
