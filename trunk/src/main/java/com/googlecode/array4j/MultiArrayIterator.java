package com.googlecode.array4j;

import java.util.Iterator;

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
