package com.googlecode.array4j.ufunc;

import java.util.Iterator;

import com.googlecode.array4j.Array2;

/**
 * Iterator for elements in an N-dimensional array.
 * <p>
 * Corresponds to <CODE>PyArrayIterObject</CODE> in the NumPy C-API.
 */
public final class ArrayIterator implements Iterable<ArrayIterator> {
    public ArrayIterator(final Array2<?> arr) {
    }

    // TODO next

    // TODO goto

    // TODO goto1d

    public Iterator<ArrayIterator> iterator() {
        return null;
    }
}
