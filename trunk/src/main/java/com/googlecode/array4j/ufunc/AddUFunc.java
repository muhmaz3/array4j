package com.googlecode.array4j.ufunc;

import com.googlecode.array4j.Array2;

public final class AddUFunc extends AbstractUFunc {
    private static final int NIN = 2;

    private static final int NOUT = 1;

    public AddUFunc() {
        super(NIN, NOUT);
    }

    protected void call(final Array2<?>[] argsin, final Array2<?>[] argsout) {
        // TODO depending on kernel, do calculation or extract buffers from
        // array and pass all the necessary bits to a JNI function
    }
}
