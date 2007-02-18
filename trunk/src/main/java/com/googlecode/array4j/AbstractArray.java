package com.googlecode.array4j;

import java.nio.Buffer;

import com.googlecode.array4j.Indexing.Index;

// TODO investigate PyArray_Where

public abstract class AbstractArray<E extends AbstractArray> implements Array2<E> {
//    private int nd; // nd = shape.length

//    private int itemsize; // TODO should be final

    // C contiguous, F contiguous, own data, aligned, writeable, updateifcopy
    private int flags;

    private int[] fShape;

    private int[] fStrides;

    private final Buffer data;

    public AbstractArray() {
        this.data = null;
    }

    protected final void reconfigureShapeStrides(final int[] shape, final int[] strides) {
        // TODO might want to copy here
        this.fShape = shape;
        this.fStrides = strides;
    }

    public final int getNdim() {
        // TODO Auto-generated method stub
        return 0;
    }

    public final int[] getShape() {
        // TODO Auto-generated method stub
        return null;
    }

    public final int getShape(final int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final int ndim() {
        // TODO Auto-generated method stub
        return 0;
    }

    public final E reshape(final int... shape) {
        // TODO Auto-generated method stub
        return null;
    }

    public final int[] shape() {
        // TODO Auto-generated method stub
        return null;
    }

    public final int shape(final int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final E getArray(final int... indexes) {
        final Object[] indexObjs = new Object[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            indexObjs[i] = indexes[i];
        }
        return get(indexObjs);
    }

    protected final void checkIndexes(final Object... indexes) {
        for (Object index : indexes) {
            if (!(index instanceof Integer) && !(index instanceof Index) && !(index instanceof int[])) {
                throw new IllegalArgumentException("invalid index type");
            }
        }
    }

    /**
     * Convert from dimensional indexes to absolute index.
     * @param indexes dimensional indexes
     * @return absolute index
     */
    protected final int indexesToIndex(final int[] indexes) {
        if (indexes.length != fShape.length) {
            throw new IllegalArgumentException();
        }
        int index = 0;
        for (int i = 0; i < indexes.length; i++) {
            int val = indexes[i];
            if (val < 0) {
                val += fShape[i];
            }
            if (val < 0 || val >= fShape[i]) {
                throw new ArrayIndexOutOfBoundsException();
            }
            index += fStrides[i] * val;
        }
        return index;
    }
}
