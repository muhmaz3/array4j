package com.googlecode.array4j.ufunc;

public final class AddUFunc extends AbstractUFunc {
    private static final int NIN = 2;

    private static final int NOUT = 1;

    public AddUFunc() {
        super(NIN, NOUT);
    }

    // TODO need functions that operate on a bunch of buffers -- can then have a
    // Java and a C implementation
}
