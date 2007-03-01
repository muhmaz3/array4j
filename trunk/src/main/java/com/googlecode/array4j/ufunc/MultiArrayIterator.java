package com.googlecode.array4j.ufunc;

import java.util.Iterator;

// TODO look in ufuncobject.h for more bits and pieces

/**
 * Iterator for broadcasting.
 * <p>
 * Corresponds to <CODE>PyArrayMultiIterObject</CODE> in the NumPy C-API.
 */
public final class MultiArrayIterator implements Iterable<ArrayIterator> {
    public Iterator<ArrayIterator> iterator() {
        return null;
    }
}
