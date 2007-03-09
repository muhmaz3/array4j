package com.googlecode.array4j.ufunc;

import java.util.Iterator;

import com.googlecode.array4j.Array;

// TODO look at PyArrayIterObject in ndarrayobject.h

/**
 * Iterator for elements in an N-dimensional array.
 * <p>
 * Corresponds to <CODE>PyArrayIterObject</CODE> in the NumPy C-API.
 */
public final class ArrayIterator implements Iterable<ArrayIterator> {
    public ArrayIterator(final Array<?> arr) {
    }

    // TODO next

    // TODO goto

    // TODO goto1d

    public Iterator<ArrayIterator> iterator() {
        return null;
    }
}
