package com.googlecode.array4j;

/**
 * Storage type.
 */
public enum Storage {
    DIRECT, HEAP;

    // TODO support compressed sparse vector for sparse BLAS level 1

    // TODO support CSR, BSR, SKYLINE, COORDINATE, CSC for sparse BLAS level 2 and 3

    public static final Storage DEFAULT_FOR_DENSE = HEAP;

    public static final Storage DEFAULT_FOR_SPARSE = null;

    public Storage checkSame(final Storage... storages) {
        for (Storage storage : storages) {
            if (!equals(storage)) {
                throw new IllegalArgumentException("all matrices must have " + this + " storage");
            }
        }
        return this;
    }
}
