package com.googlecode.array4j;

import com.googlecode.array4j.Indexing.Index;

public abstract class AbstractArray<E extends AbstractArray> implements Array2<E> {
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

    protected final void checkIndexes(final Object... indexes) {
        for (Object index : indexes) {
            if (!(index instanceof Integer) && !(index instanceof Index)) {
                throw new IllegalArgumentException("invalid index type");
            }
        }
    }
}
