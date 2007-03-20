package com.googlecode.array4j.ufunc;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.googlecode.array4j.ArrayUtils;
import com.googlecode.array4j.DenseArray;

/**
 * Iterator for broadcasting.
 * <p>
 * Corresponds to <CODE>PyArrayMultiIterObject</CODE> in the NumPy C-API.
 */
public final class MultiArrayIterator implements Iterable<MultiArrayIterator>, Iterator<MultiArrayIterator> {
    private int fSize;

    private int fIndex;

    private int[] fDimensions;

    private final ArrayIterator[] fIters;

    public MultiArrayIterator(final int numiter) {
        if (numiter < 2) {
            throw new IllegalArgumentException("Need at least 2 array objects");
        }
        this.fIters = new ArrayIterator[numiter];
    }

    public MultiArrayIterator(final int numiter, final DenseArray... arrs) {
        this(numiter);
        if (arrs.length > numiter) {
            throw new IllegalArgumentException("Too many array objects");
        }
        for (int i = 0; i < arrs.length; i++) {
            createArrayIterator(i, arrs[i]);
        }
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

    public Iterator<MultiArrayIterator> iterator() {
        return this;
    }

    public void createArrayIterator(final int index, final DenseArray arr) {
        fIters[index] = new ArrayIterator(arr);
    }

    public ArrayIterator getIterator(final int index) {
        return fIters[index];
    }

    public int numiter() {
        return fIters.length;
    }

    public int size() {
        return fSize;
    }

    public int index() {
        return fIndex;
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
                // This prepends 1 to shapes not already equal to nd
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

    /**
     * Adjusts previously broadcasted iterators so that the axis with the
     * smallest sum of iterator strides is not iterated over. Returns dimension
     * which is smallest in the range <tt>[0, ndim())</tt>. A <tt>-1</tt>
     * is returned if <tt>ndim() == 0</tt>.
     * <p>
     * This method corresponds to the NumPy function <CODE>PyArray_RemoveSmallest</CODE>.
     */
    public int removeSmallest() {
        if (ndim() == 0) {
            return -1;
        }
        final int ndim = ndim();
        final int[] sumstrides = new int[ndim];
        for (int i = 0; i < ndim; i++) {
            for (int j = 0; j < fIters.length; j++) {
                sumstrides[i] += fIters[j].strides(j);
            }
        }
        int axis = 0;
        int smallest = sumstrides[0];
        /* Find longest dimension */
        for (int i = 1; i < ndim; i++) {
            if (sumstrides[i] < smallest) {
                axis = i;
                smallest = sumstrides[i];
            }
        }

        for (int i = 0; i < fIters.length; i++) {
            fIters[i].removeAxis(axis);
        }

        fSize = fIters[0].size();
        return axis;
    }

    /**
     * Optimize axis the iteration takes place over.
     * <p>
     * The first thought was to have the loop go over the largest dimension to
     * minimize the number of loops.
     * <p>
     * However, on processors with slow memory bus and cache, the slowest loops
     * occur when the memory access occurs for large strides.
     * <p>
     * Thus, choose the axis for which strides of the last iterator is smallest
     * but non-zero.
     */
    int optimizeAxis() {
        final int nd = ndim();
        final int[] stridesum = new int[nd];
        for (int i = 0; i < nd; i++) {
            stridesum[i] = 0;
            for (int j = 0; j < fIters.length; j++) {
                stridesum[i] += fIters[j].strides(i);
            }
        }

        int ldim = nd - 1;
        int minsum = stridesum[nd - 1];
        for (int i = nd - 2; i >= 0; i--) {
            if (stridesum[i] < minsum) {
                ldim = i;
                minsum = stridesum[i];
            }
        }

        final int maxdim = fDimensions[ldim];
        fSize /= maxdim;

        for (int i = 0; i < fIters.length; i++) {
            fIters[i].optimizeAxis(ldim);
        }

        return ldim;
    }
}
