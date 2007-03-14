package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.ArrayType;

final class AddUFunc extends AbstractUFunc {
    private static final int NIN = 2;

    private static final int NOUT = 1;

    private static final Signature[] SIGNATURES = {
        new Signature(ArrayType.DOUBLE, ArrayType.DOUBLE, ArrayType.DOUBLE)
    };

    public AddUFunc() {
        super(null, SIGNATURES, NIN, NOUT, Identity.ZERO);
    }
}
