package com.googlecode.array4j;

import java.nio.Buffer;
import java.util.Arrays;

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
    private enum Flags {
        /**
         * Means c-style contiguous (last index varies the fastest). The data
         * elements right after each other.
         */
        CONTIGUOUS(0x0001),
        /**
         * set if array is a contiguous Fortran array: the first index varies
         * the fastest in memory (strides array is reverse of C-contiguous
         * array).
         */
        FORTRAN(0x0002),
        C_CONTIGUOUS(CONTIGUOUS),
        F_CONTIGUOUS(FORTRAN),
        OWNDATA(0x0004),
        FORCECAST(0x0010),
        /**
         * Always copy the array. Returned arrays are always CONTIGUOUS,
         * ALIGNED, and WRITEABLE.
         */
        ENSURECOPY(0x0020),
        ENSUREARRAY(0x0040),
        /**
         * Make sure that the strides are in units of the element size Needed
         * for some operations with record-arrays.
         */
        ELEMENTSTRIDES(0x0080),
        /**
         * Array data is aligned on the appropiate memory address for the type
         * stored according to how the compiler would align things (e.g., an
         * array of integers (4 bytes each) starts on a memory address that's a
         * multiple of 4).
         */
        ALIGNED(0x0100),
        /** Array data has the native endianness. */
        NOTSWAPPED(0x0200),
        /** Array data is writeable. */
        WRITEABLE(0x0400),
        /*
         * If this flag is set, then base contains a pointer to an array of the
         * same size that should be updated with the current contents of this
         * array when this array is deallocated
         */
        UPDATEIFCOPY(0x1000),
        /* This flag is for the array interface */
        ARR_HAS_DESCR(0x0800),
        BEHAVED (ALIGNED, WRITEABLE),
        BEHAVED_NS (ALIGNED, WRITEABLE, NOTSWAPPED),
        CARRAY (CONTIGUOUS, BEHAVED),
        CARRAY_RO (CONTIGUOUS, ALIGNED),
        FARRAY (FORTRAN, BEHAVED),
        FARRAY_RO (FORTRAN, ALIGNED),
        DEFAULT(CARRAY),
        IN_ARRAY(CARRAY_RO),
        OUT_ARRAY(CARRAY),
        INOUT_ARRAY(CARRAY, UPDATEIFCOPY),
        IN_FARRAY(FARRAY_RO),
        OUT_FARRAY(FARRAY),
        INOUT_FARRAY (FARRAY, UPDATEIFCOPY),
        UPDATE_ALL (CONTIGUOUS, FORTRAN, ALIGNED);
        private int value;
        Flags(final int value) {
            this.value = value;
        }
        Flags(final Flags... values) {
            for (final Flags value: values) {
                this.value |= value.value;
            }
        }
        public int intValue() {
            return value;
        }
        public boolean and(final Flags... flags) {
            for (final Flags flag : flags) {
                if ((value & flag.value) == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final int PSEUDO_INDEX = -1;

    private static final int RUBBER_INDEX = -2;

    private static final int SINGLE_INDEX = -3;

//    private int nd; // nd = shape.length

//    private int itemsize; // TODO should be final

    // C contiguous, F contiguous, own data, aligned, writeable, updateifcopy
    private Flags flags;

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

    /**
     * Returns an appropriate strides array if all we are doing is inserting
     * ones into the shape, or removing ones from the shape or doing a
     * combination of the two.
     * <p>
     * In this case we don't need to do anything but update strides and
     * dimensions. So, we can handle non single-segment cases.
     */
    private int[] checkStridesOnes(final int[] newdims) {
        boolean done = false;
        final int nd = fShape.length;
        final int newnd = newdims.length;
        final int[] newstrides = new int[newnd];
        for (int k = 0, j = 0; !done && (j < nd || k < newnd);) {
            if ((j < nd) && (k < newnd) && (newdims[k] == fShape[j])) {
                newstrides[k] = fStrides[j];
                j++;
                k++;
            } else if ((k < newnd) && (newdims[k] == 1)) {
                newstrides[k] = 0;
                k++;
            } else if ((j < nd) && (fShape[j] == 1)) {
                j++;
            } else {
                done = true;
            }
        }
        if (done) {
            return null;
        } else {
            return newstrides;
        }
    }

    /**
     * Fix any -1 dimensions and check new dimensions against old size.
     */
    private void fixUnknownDimension(final int[] dimensions) {
        final String msg = "total size of new array must be unchanged";
        final int n = dimensions.length;
        int sknown = 1;
        int iunknown = -1;
        final int soriginal = size();
        for (int i = 0; i < n; i++) {
            if (dimensions[i] < 0) {
                if (iunknown == -1) {
                    iunknown = i;
                } else {
                    throw new IllegalArgumentException("can only specify one unknown dimension");
                }
            } else {
                sknown *= dimensions[i];
            }
        }
        if (iunknown >= 0) {
            if ((sknown == 0) || (soriginal % sknown != 0)) {
                throw new IllegalArgumentException(msg);
            }
            dimensions[iunknown] = soriginal / sknown;
        } else {
            if (soriginal != sknown) {
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public final E reshape(final int... newdims) {
        if (newdims == null || Arrays.equals(fShape, newdims)) {
            // TODO might want to return a new object here
            return (E) this;
        }

        int[] strides = checkStridesOnes(newdims);

        /* Now we are really reshaping and not just adding ones to the shape somewhere. */

        final int n = newdims.length;
        if (strides == null) {
            fixUnknownDimension(newdims);
        } else if (n > 0) {
            /*
             * Replace any 0-valued strides with appropriate value to preserve
             * contiguousness.
             */
            if (false) {
                // TODO check for fortran order
            } else {
                if (strides[n - 1] == 0) {
                    // TODO numpy sets value to self->descr->elsize
                    strides[n - 1] = 1;
                }
                for (int i = n - 2; i >= -1; i--) {
                    if (strides[i] == 0) {
                        strides[i] = strides[i + 1] * newdims[i + 1];
                    }
                }
            }
        }

        return null;
    }

    // TODO reshape that takes an order argument

    public final int[] shape() {
        // TODO Auto-generated method stub
        return null;
    }

    public final int shape(final int index) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final int size() {
        // TODO only calculate this once
        // TODO code very similar to what's going on in calculateSize
        int size = 1;
        for (int dim : fShape) {
            size *= dim;
        }
        return size;
    }

    public final E getArray(final int... indexes) {
        final Object[] indexObjs = new Object[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            indexObjs[i] = indexes[i];
        }
        return get(indexObjs);
    }

    // XXX derived types must override this all the way to the top
    protected abstract E create(final E other);

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
        private int start;

        private int nsteps;

        private int stepSize;
    }

    protected final SubIndex parseSubIndex(final Object index, final int max) {
        int start;
        int nsteps;
        int stepSize;
        if (Indexing.newaxis() == index) {
            start = 0;
            nsteps = PSEUDO_INDEX;
            stepSize = 0;
        } else if (Indexing.ellipsis() == index) {
            start = 0;
            nsteps = RUBBER_INDEX;
            stepSize = 0;
        } else if (index instanceof Slice) {
            final Slice slice = (Slice) index;
            start = slice.getStart(max);
            stepSize = slice.getStep();
            nsteps = slice.getSliceLength(max);
            if (nsteps <= 0) {
                start = 0;
                stepSize = 1;
                nsteps = 0;
            }
        } else {
            start = (Integer) index;
            nsteps = SINGLE_INDEX;
            stepSize = 0;
            if (start < 0) {
                start += max;
            }
            if (start < 0 || start >= max) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        final SubIndex subindex = new SubIndex();
        subindex.start = start;
        subindex.nsteps = nsteps;
        subindex.stepSize = stepSize;
        return subindex;
    }

    protected final int parseIndexes(final Object[] indexes) {
        checkIndexes(indexes);

        final E newArr = create(null);

        final int n = indexes.length;
        int ndold = 0;
        int ndnew = 0;
        int nadd = 0;
        int offset = 0;
        for (int i = 0; i < n; i++) {
            final int max = ndold < fShape.length ? fShape[ndold] : 0;
            final SubIndex subindex = parseSubIndex(indexes[i], max);
            if (PSEUDO_INDEX == subindex.nsteps) {
                newArr.fShape[ndnew] = 1;
                newArr.fStrides[ndnew] = 0;
                ndnew++;
            } else if (RUBBER_INDEX == subindex.nsteps) {
                int npseudo = 0;
                for (int j = i + 1; j < n; j++) {
                    if (Indexing.newaxis() == indexes[j]) {
                        npseudo++;
                    }
                }
                nadd = fShape.length - (n - i - npseudo - 1 + ndold);
                if (nadd < 0) {
                    throw new IllegalArgumentException("too many indices");
                }
                for (int j = 0; j < nadd; j++) {
                    newArr.fShape[ndnew] = fShape[ndold];
                    newArr.fStrides[ndnew] = fStrides[ndold];
                    ndnew++;
                    ndold++;
                }
            } else {
                if (ndold >= fShape.length) {
                    throw new IllegalArgumentException("too many indices");
                }
                offset += fStrides[ndold] * subindex.start;
                ndold++;
                if (subindex.nsteps != SINGLE_INDEX) {
                    newArr.fShape[ndnew] = subindex.nsteps;
                    newArr.fStrides[ndnew] = subindex.stepSize * fStrides[ndold - 1];
                    ndnew++;
                }
            }
        }
        nadd = fShape.length - ndold;
        for (int j = 0; j < nadd; j++) {
            newArr.fShape[ndnew] = fShape[ndold];
            newArr.fStrides[ndnew] = fStrides[ndold];
            ndnew++;
            ndold++;
        }

        return offset;
    }

    private static int calculateSize(final int[] shape) {
        int capacity = 1;
        for (int dim : shape) {
            if (dim < 0) {
                throw new IllegalArgumentException();
            }
            // TODO should we worry about overflow here?
            capacity *= dim;
        }
        return capacity;
    }

    public final boolean isOneSegment() {
        return fShape.length == 0 || flags.and(Flags.CONTIGUOUS) || flags.and(Flags.FORTRAN);
    }
}
