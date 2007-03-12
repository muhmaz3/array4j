package com.googlecode.array4j.ufunc;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.ArrayUtils;

/**
 * Iterator for broadcasting.
 * <p>
 * Corresponds to <CODE>PyArrayMultiIterObject</CODE> in the NumPy C-API.
 */
public final class MultiArrayIterator implements Iterator<MultiArrayIterator> {
    private int fSize;

    private int fIndex;

    private int[] fDimensions;

    private final ArrayIterator[] fIters;

    public MultiArrayIterator(final int numiter) {
        this.fIters = new ArrayIterator[numiter];
    }

    public boolean hasNext() {
        return fIndex < fSize;
    }

    public MultiArrayIterator next() {
        if (fIndex == fSize) {
            throw new NoSuchElementException();
        }

        /* Adjust loop pointers */
        for (ArrayIterator iter : fIters) {
            iter.next();
        }

        fIndex++;
        return this;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void createArrayIterator(final int index, final Array<?> arr) {
        fIters[index] = new ArrayIterator(arr);
    }

    public int numiter() {
        return fIters.length;
    }

    public int size() {
        return fSize;
    }

    public int[] shape() {
        // TODO this should technically return a copy
        return fDimensions;
    }

    public int shape(final int index) {
        return fDimensions[index];
    }

    public int ndim() {
        return fDimensions.length;
    }

    public void broadcast(final int numiter) {
        /* Discover the broadcast number of dimensions */
        int nd = 0;
        for (int i = 0; i < numiter; i++) {
            nd = Math.max(nd, fIters[i].ndim());
        }

        /* Discover the broadcast shape in each dimension */
        fDimensions = new int[nd];
        for (int i = 0; i < nd; i++) {
            fDimensions[i] = 1;
            for (int j = 0; j < numiter; j++) {
                final ArrayIterator it = fIters[i];
                final int k = i + it.ndim() - nd;
                if (k >= 0) {
                    final int tmp = it.shape(k);
                    if (tmp == 1) {
                        continue;
                    }
                    if (fDimensions[i] == 1) {
                        fDimensions[i] = tmp;
                    } else if (fDimensions[i] != tmp) {
                        throw new IllegalArgumentException(
                                "shape mismatch: objects cannot be broadcast to a single shape");
                    }
                }
            }
        }

        fSize = ArrayUtils.multiplyList(fDimensions);

        /*
         * Reset the iterator dimensions and strides of each iterator object --
         * using 0 valued strides for broadcasting
         */
        for (int i = 0; i < numiter; i++) {
            fIters[i].resetAfterBroadcast(this);
        }
    }
}
