package com.googlecode.array4j;

import java.nio.Buffer;

import com.googlecode.array4j.Indexing.Index;

// TODO investigate PyArray_Where

/*
 * NumPy's get algorithm:
 * <CODE>
 * if (op is integer) {
 *     array_item_nice:
 *     if (nd == 1) {
 *         return array scalar using index2ptr
 *     } else {
 *         use index2ptr to get get pointer to data
 *         returned array has nd - 1; first dimension and stride are stripped off
 *         return array_big_item(i) through PyArray_Return
 *     }
 * }
 * if (op is tuple with exactly nd elements) {
 *     return array scalar using PyArray_GetPtr to get value
 * }
 *
 * else: call array_subscript with op, which does:
 *     if (PyString_Check(op) || PyUnicode_Check(op)) {
 *         look for a field named op
 *     }
 *     if nd == 0 {
 *         if op == ellipsis
 *             return self (for array scalars)
 *         if op == None
 *             return self with a new axis
 *         if op is a tuple
 *             if tuple is empty
 *                 return self
 *             if ((nd = count_new_axes_0d(op)) == -1)
 *                 return NULL;
 *             return add_new_axes_0d(self, nd);
 *     }
 *     check if op implies fancy indexing
 *     if (op is fancy) {
 *         ...
 *     }
 *     else: call array_subscript_simple, which does:
 *         if (op can be converted to intp) {
 *             return array_big_item(op as intp)
 *         }
 *         // this can return an error
 *         calculate nd = parse_index(self, op, dimensions, strides, &offset)
 *         parse_index does:
 *             if op is slice, ellipsis or None:
 *                 incref op
 *                 set n = 1
 *                 set is_slice to 1
 *             else:
 *                 if op is not a sequence, throw
 *                 n = sequence length of op
 *                 is_slice = 0
 *             for (int i : 0 -> i < n)
 *                 if (!is_slice)
 *                     check that i is a valid index into op (which is a known to be a sequence)
 *                 start = parse_subindex(op1, &step_size, &n_steps, XXX conditional)
 *                 if start == -1: break
 *                 XXX big conditional
 *             XXX another conditional
 *
 *
 *
 *         return array with new nd, same dimensions, same strides, data pointer at: data+offset
 *
 * if array_subscript returned array with nd == 0:
 *     do various checks related to op being an ellipsis
 *     return an array scalar in some cases
 *
 * </CODE>
 */

public abstract class AbstractArray<E extends AbstractArray> implements Array2<E> {
    private static final int PSEUDO_INDEX = -1;

    private static final int RUBBER_INDEX = -2;

    private static final int SINGLE_INDEX = -3;

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
        if (indexes.length == 0) {
            throw new IllegalArgumentException("msut specify at least one index");
        }
        for (Object index : indexes) {
            // TODO support Lists in fancy indexing
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

    private class SubIndex {
        public int start;
        public int nsteps;
        public int stepSize;
    }
    
    protected final int parseSubIndex(final Object index, final int max) {
        int start = 0;
        int nsteps;
        int stepSize;
        if (index == Indexing.newaxis()) {
            nsteps = PSEUDO_INDEX;
            start = 0;
        } else if (index == Indexing.ellipsis()) {
            nsteps = RUBBER_INDEX;
            start = 0;
        } else if (index instanceof Slice) {
            // TODO check slice and maybe set some stuff
            nsteps = -99;
            if (nsteps <= 0) {
                nsteps = 0;
                stepSize = 1;
                start = 0;
            }
        } else {
            start = (Integer) index;
            nsteps = SINGLE_INDEX;
            if (start < 0) {
                start += max;
            }
            if (start < 0 || start >= max) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return start;
    }

    protected final void parseIndexes(final Object[] indexes) {
        checkIndexes(indexes);
        int ndold = 0;
        int ndnew = 0;
        for (int i = 0; i < indexes.length; i++) {
            final int max = ndold < fShape.length ? fShape[ndold] : 0;
            final int start = parseSubIndex(indexes[i], max);
            // TODO also need nsteps here
        }

        // TODO calculate new shape and offset into buffer for view
    }
}
