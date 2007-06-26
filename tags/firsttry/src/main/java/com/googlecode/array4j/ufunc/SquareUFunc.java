package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayFunctions;
import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.kernel.Interface;

final class SquareUFunc extends AbstractUFunc {
    private static final int NIN = 1;

    private static final int NOUT = 1;

    private static final Signature[] SIGNATURES = {new Signature(new ArrayType[]{ArrayType.DOUBLE},
            new ArrayType[]{ArrayType.DOUBLE}, Interface.defaultKernel().getDoubleFunctions())};

    public SquareUFunc() {
        super(null, SIGNATURES, NIN, NOUT, Identity.NONE);
    }

    @Override
    public void call(final ArrayFunctions functions, final ByteBuffer[] bufptr, final int[] dimensions,
            final int[] steps, final Object funcdata) {
        functions.square(bufptr, dimensions, steps, funcdata);
    }
}
