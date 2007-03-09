package com.googlecode.array4j;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.googlecode.array4j.Indexing.Index;
import com.googlecode.array4j.kernel.Interface;
import com.googlecode.array4j.kernel.KernelType;
import com.googlecode.array4j.types.ArrayDescr;
import com.googlecode.array4j.ufunc.AddUFunc;
import com.googlecode.array4j.ufunc.UFunc;

public abstract class AbstractArray<E extends AbstractArray> implements Array2<E> {
    private static final int PSEUDO_INDEX = -1;

    private static final int RUBBER_INDEX = -2;

    private static final int SINGLE_INDEX = -3;

    private static final int MAX_DIMS = 32;

    private int[] fDimensions;

    private int[] fStrides;

    private final ByteBuffer fData;

    private final KernelType fKernelType;

    private int fFlags;

    private final ArrayDescr fDescr;

    private final Object fBase;

    /**
     * Simple constructor.
     * <p>
     * This constructor corresponds to the <CODE>PyArray_SimpleNewFromDescr</CODE>
     * function in NumPy.
     *
     * @param dims
     *            dimensions
     */
    protected AbstractArray(final ArrayDescr descr, final int[] dims) {
        this(descr, dims, Flags.EMPTY.getValue());
    }

    protected AbstractArray(final ArrayDescr descr, final int[] dims, final int flags) {
        this(descr, dims, null, null, flags, null, KernelType.DEFAULT);
    }

    protected AbstractArray(final ArrayDescr descr, final int[] dims, final int[] strides, final int flags) {
        this(descr, dims, strides, null, flags, null, KernelType.DEFAULT);
    }

    /**
     * Array constructor.
     * <p>
     * This constructor corresponds to the <CODE>PyArray_NewFromDescr</CODE>
     * function in NumPy.
     *
     * @param descr
     *            array description
     * @param dims
     *            dimensions
     * @param strides
     *            strides
     * @param data
     *            flag indicating whether array has its own buffer
     * @param flags
     *            flags
     * @param base
     *            base array
     * @param kernelType
     *            kernel to use when allocating data buffer
     */
    protected AbstractArray(final ArrayDescr descr, final int[] dims, final int[] strides, final ByteBuffer data,
            final int flags, final Object base, final KernelType kernelType) {
        if (descr.getSubArray() != null) {
            throw new UnsupportedOperationException();
        }
        // TODO make sure ArrayDescr is immutable
        fDescr = descr;

        // TODO set base array properly
        fBase = null;

        /* Check dimensions. */
        final int nd = dims != null ? dims.length : 0;
        int size = 1;
        int sd = elementSize();
        if (sd == 0) {
            throw new UnsupportedOperationException();
        }
        final int largest = Integer.MAX_VALUE / sd;
        for (int i = 0; i < nd; i++) {
            if (dims[i] == 0) {
                continue;
            }
            if (dims[i] < 0) {
                throw new IllegalArgumentException("negative dimensions are not allowed");
            }
            size *= dims[i];
            if (size <= 0 || size > largest) {
                throw new IllegalArgumentException("dimensions too large");
            }
        }

        int arrayFillFlags = flags;
        if (data == null) {
            fFlags = Flags.DEFAULT.getValue();
            if (flags != 0) {
                fFlags |= Flags.FORTRAN.getValue();
                if (nd > 1) {
                    fFlags &= ~Flags.CONTIGUOUS.getValue();
                }
                arrayFillFlags = Flags.FORTRAN.getValue();
            }
        } else {
            fFlags = flags & ~Flags.UPDATEIFCOPY.getValue();
        }

        if (nd > 0) {
            this.fDimensions = copyOf(dims);
            if (strides == null) {
                fStrides = new int[nd];
                sd = arrayFillStrides(dims, sd, arrayFillFlags);
            } else {
                /*
                 * we allow strides even when we create the memory, but be
                 * careful with this...
                 */
                fStrides = copyOf(strides);
                sd *= size;
            }
        } else {
            fDimensions = null;
            fStrides = null;
        }

        if (data == null) {
            if (sd == 0) {
                sd = elementSize();
            }
            fData = Interface.kernel(kernelType).createBuffer(sd);
            fFlags |= Flags.OWNDATA.getValue();
        } else {
            fFlags &= ~Flags.OWNDATA.getValue();
            fData = data;
        }
        // set the typed buffer in the derived type
        setBuffer(fData);
        fKernelType = kernelType;
    }

//    private void foo(final Class<? extends GenericType<?>> dtypeClass) {
//    }
//
//    private <T extends ArrayType<T>> void foo(final T dtype) {
//    }

    private int arrayFillStrides(final int[] dims, final int sd, final int inflag) {
        final int nd = dims.length;
        int itemsize = sd;
        if (Flags.FORTRAN.and(inflag) && !Flags.CONTIGUOUS.and(inflag)) {
            for (int i = 0; i < nd; i++) {
                fStrides[i] = itemsize;
                itemsize *= dims[i] != 0 ? dims[i] : 1;
            }
            fFlags |= Flags.FORTRAN.getValue();
            if (nd > 1) {
                fFlags &= ~Flags.CONTIGUOUS.getValue();
            } else {
                fFlags |= Flags.CONTIGUOUS.getValue();
            }
        } else {
            for (int i = nd - 1; i >= 0; i--) {
                fStrides[i] = itemsize;
                itemsize *= dims[i] != 0 ? dims[i] : 1;
            }
            fFlags |= Flags.CONTIGUOUS.getValue();
            if (nd > 1) {
                fFlags &= ~Flags.FORTRAN.getValue();
            } else {
                fFlags |= Flags.FORTRAN.getValue();
            }
        }
        return itemsize;
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
        final int nd = fDimensions.length;
        final int newnd = newdims.length;
        final int[] newstrides = new int[newnd];
        for (int k = 0, j = 0; !done && (j < nd || k < newnd);) {
            if ((j < nd) && (k < newnd) && (newdims[k] == fDimensions[j])) {
                newstrides[k] = fStrides[j];
                j++;
                k++;
            } else if ((k < newnd) && (newdims[k] == 1)) {
                newstrides[k] = 0;
                k++;
            } else if ((j < nd) && (fDimensions[j] == 1)) {
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

    private int[] attemptNoCopyReshape(final int[] newdims, final Order fortran) {
        final int newnd = newdims.length;
        final int[] olddims = new int[fDimensions.length];
        final int[] oldstrides = new int[fStrides.length];
        final int[] newstrides = new int[newdims.length];

        int oldnd = 0;
        for (int oi = 0; oi < ndim(); oi++) {
            if (fDimensions[oi] != 1) {
                olddims[oldnd] = fDimensions[oi];
                oldstrides[oldnd] = fStrides[oi];
                oldnd++;
            }
        }

        int np = 1;
        for (int ni = 0; ni < newnd; ni++) {
            np *= newdims[ni];
        }

        int op = 1;
        for (int oi = 0; oi < oldnd; oi++) {
            op *= olddims[oi];
        }

        if (np != op) {
            /* different total sizes; no hope */
            return null;
        }

        int oi = 0;
        int oj = 1;
        int ni = 0;
        int nj = 1;

        while (ni < newnd && oi < oldnd) {
            np = newdims[ni];
            op = olddims[oi];

            while (np != op) {
                if (np < op) {
                    np *= newdims[nj++];
                } else {
                    op *= olddims[oj++];
                }
            }

            for (int ok = oi; ok < oj - 1; ok++) {
                if (fortran.equals(Order.FORTRAN)) {
                    if (oldstrides[ok + 1] != olddims[ok] * oldstrides[ok]) {
                        /* not contiguous enough */
                        return null;
                    }
                } else {
                    if (oldstrides[ok] != olddims[ok + 1] * oldstrides[ok + 1]) {
                        /* not contiguous enough */
                        return null;
                    }
                }
            }

            if (fortran.equals(Order.FORTRAN)) {
                newstrides[ni] = oldstrides[oi];
                for (int nk = ni + 1; nk < nj; nk++) {
                    newstrides[nk] = newstrides[nk - 1] * newdims[nk - 1];
                }
            } else {
                newstrides[nj - 1] = oldstrides[oj - 1];
                for (int nk = nj - 1; nk > ni; nk--) {
                    newstrides[nk - 1] = newstrides[nk] * newdims[nk];
                }
            }

            ni = nj++;
            oi = oj++;
        }

        return newstrides;
    }

    public final E reshape(final int... newdims) {
        return reshape(Order.C, newdims);
    }

    public final E reshape(final Order order, final int... newdims) {
        final Order fortran = chooseOrder(order);

        /*  Quick check to make sure anything actually needs to be done */
        if (newdims == null || Arrays.equals(fDimensions, newdims)) {
            // TODO might want to return a new object here
            return (E) this;
        }

        int[] strides = checkStridesOnes(newdims);

        int flags = fFlags;

        /* Now we are really reshaping and not just adding ones to the shape somewhere. */
        final int n = newdims.length;
        if (strides == null) {
            fixUnknownDimension(newdims);
            if (!(isOneSegment())
                    || (((checkFlags(Flags.CONTIGUOUS) && fortran == Order.FORTRAN)
                            || (checkFlags(Flags.FORTRAN) && fortran == Order.C)) && (ndim() > 1))) {
                final int[] newstrides = attemptNoCopyReshape(newdims, fortran);
                if (newstrides != null) {
                    strides = newstrides;
                    flags = fFlags;
                } else {
                    // TODO make a copy
                    flags = fFlags;
                    throw new UnsupportedOperationException();
                }
            }

            /* We always have to interpret the contiguous buffer correctly. */

            /* Make sure the flags argument is set. */
            if (n > 1) {
                if (fortran.equals(Order.FORTRAN)) {
                    flags &= ~Flags.CONTIGUOUS.getValue();
                    flags |= Flags.FORTRAN.getValue();
                } else {
                    flags &= ~Flags.FORTRAN.getValue();
                    flags |= Flags.CONTIGUOUS.getValue();
                }
            }
        } else if (n > 0) {
            /*
             * Replace any 0-valued strides with appropriate value to preserve
             * contiguousness.
             */
            if (fortran.equals(Order.FORTRAN)) {
                if (strides[0] == 0) {
                    strides[0] = elementSize();
                    throw new UnsupportedOperationException();
                }
                for (int i = 1; i < n; i++) {
                    if (strides[i] == 0) {
                        strides[i] = strides[i - 1] * newdims[i - 1];
                    }
                }
            } else {
                if (strides[n - 1] == 0) {
                    strides[n - 1] = elementSize();
                    throw new UnsupportedOperationException();
                }
                for (int i = n - 2; i >= -1; i--) {
                    if (strides[i] == 0) {
                        strides[i] = strides[i + 1] * newdims[i + 1];
                    }
                }
            }
        }

        return create(newdims, strides, (ByteBuffer) fData.duplicate().rewind(), flags, fKernelType);
    }

    public final int[] shape() {
        final int[] shapeCopy = new int[fDimensions.length];
        System.arraycopy(fDimensions, 0, shapeCopy, 0, fDimensions.length);
        return shapeCopy;
    }

    public final int shape(final int index) {
        return fDimensions[index];
    }

    public final int[] strides() {
        final int[] stridesCopy = new int[fStrides.length];
        System.arraycopy(fStrides, 0, stridesCopy, 0, fStrides.length);
        return stridesCopy;
    }

    public final int strides(final int index) {
        return fStrides[index];
    }

    public final int size() {
        int size = 1;
        for (int dim : fDimensions) {
            // checks in constructor will ensure that this never overflows
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

    /**
     * Check if integer indexes are valid and calculate offset into array.
     * <p>
     * This function is based on the code in the NumPy function <CODE>array_subscript_nice</CODE>
     * where it does "optimization for a tuple of integers".
     */
    protected final int getOffsetFromIndexes(final int[] indexes) {
        if (indexes.length != fDimensions.length) {
            throw new IllegalArgumentException("invalid number of indexes");
        }

        final int[] vals = new int[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            vals[i] = indexes[i];
            if (vals[i] < 0) {
                vals[i] += fDimensions[i];
            }
            if (vals[i] < 0 || vals[i] >= fDimensions[i]) {
                throw new ArrayIndexOutOfBoundsException("index " + vals[i] + " out of range (0<=index<="
                        + fDimensions[i] + ") in dimension " + i);
            }
        }

        // This code corresponds to the PyArray_GetPtr function in NumPy.
        final int nd = fDimensions.length;
        int offset = 0;
        for (int i = 0; i < nd; i++) {
            offset += fStrides[i] * vals[i];
        }
        return offset;
    }

    protected final void checkIndexes(final Object... indexes) {
        if (indexes.length == 0) {
            throw new IllegalArgumentException("must specify at least one index");
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
        if (indexes.length != fDimensions.length) {
            throw new IllegalArgumentException();
        }
        int index = 0;
        for (int i = 0; i < indexes.length; i++) {
            int val = indexes[i];
            if (val < 0) {
                val += fDimensions[i];
            }
            if (val < 0 || val >= fDimensions[i]) {
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

    public final E get(final Object... indexes) {
        checkIndexes(indexes);
        final String msg = "too many indices";
        final int n = indexes.length;
        int ndold = 0;
        int ndnew = 0;
        int nadd = 0;
        int offset = 0;

        // TODO see if we can avoid this hard-coded size up front
        final int[] dims = new int[MAX_DIMS];
        final int[] strides = new int[MAX_DIMS];

        for (int i = 0; i < n; i++) {
            final int max = ndold < fDimensions.length ? fDimensions[ndold] : 0;
            final SubIndex subindex = parseSubIndex(indexes[i], max);
            if (PSEUDO_INDEX == subindex.nsteps) {
                dims[ndnew] = 1;
                strides[ndnew] = 0;
                ndnew++;
            } else if (RUBBER_INDEX == subindex.nsteps) {
                int npseudo = 0;
                for (int j = i + 1; j < n; j++) {
                    if (Indexing.newaxis() == indexes[j]) {
                        npseudo++;
                    }
                }
                nadd = fDimensions.length - (n - i - npseudo - 1 + ndold);
                if (nadd < 0) {
                    throw new IllegalArgumentException(msg);
                }
                for (int j = 0; j < nadd; j++) {
                    dims[ndnew] = fDimensions[ndold];
                    strides[ndnew] = fStrides[ndold];
                    ndnew++;
                    ndold++;
                }
            } else {
                if (ndold >= fDimensions.length) {
                    throw new IllegalArgumentException(msg);
                }
                offset += fStrides[ndold] * subindex.start;
                ndold++;
                if (subindex.nsteps != SINGLE_INDEX) {
                    dims[ndnew] = subindex.nsteps;
                    strides[ndnew] = subindex.stepSize * fStrides[ndold - 1];
                    ndnew++;
                }
            }
        }
        nadd = fDimensions.length - ndold;
        for (int j = 0; j < nadd; j++) {
            dims[ndnew] = fDimensions[ndold];
            strides[ndnew] = fStrides[ndold];
            ndnew++;
            ndold++;
        }

        final int[] newdims = new int[ndnew];
        System.arraycopy(dims, 0, newdims, 0, ndnew);
        final int[] newstrides = new int[ndnew];
        System.arraycopy(strides, 0, newstrides, 0, ndnew);

        return create(newdims, newstrides, (ByteBuffer) fData.position(offset), fFlags, fKernelType);
    }

    public final int nbytes() {
        return fData.capacity();
    }

    public final int ndim() {
        return fDimensions.length;
    }

    public final int elementSize() {
        return fDescr.getElementSize();
    }

    public final int flags() {
        return fFlags;
    }

    private boolean checkFlags(final Flags... flags) {
        for (final Flags flag : flags) {
            if (!flag.and(fFlags)) {
                return false;
            }
        }
        return true;
    }

    public final boolean isOneSegment() {
        return ndim() == 0 || Flags.CONTIGUOUS.and(fFlags) || Flags.FORTRAN.and(fFlags);
    }

    public final boolean isFortran() {
        return Flags.FORTRAN.and(fFlags) && ndim() > 1;
    }

    public final boolean isWriteable() {
        return Flags.WRITEABLE.and(fFlags);
    }

    public final E addEquals(final Array2<?> arr) {
        if (!(arr instanceof AbstractArray)) {
            throw new UnsupportedOperationException();
        }

        UFunc ufunc = new AddUFunc();
        ufunc.call(this, arr, this);

        return (E) this;
    }

    private Order chooseOrder(final Order order) {
        if (order == Order.ANY) {
            if (isFortran()) {
                return Order.FORTRAN;
            } else {
                return Order.C;
            }
        } else {
            return order;
        }
    }

    private static int[] copyOf(final int[] arr) {
        final int[] newarr = new int[arr.length];
        System.arraycopy(arr, 0, newarr, 0, arr.length);
        return newarr;
    }

    protected static class Range {
        public int length;

        public double start;

        public double stop;

        public double step;

        public double next;
    }

    /**
     * Calculate the length and next = start + step.
     */
    protected static Range calculateRange(final double start, final double stop, final double step) {
        double next = stop - start;
        // "true" division
        final double val = next / step;
        int len;
        if (false) {
            // TODO support range calculation for complex types
            throw new UnsupportedOperationException();
        } else {
            len = (int) Math.ceil(val);
        }
        if (len > 0) {
            next = start + step;
        }
        if (len < 0) {
            len = 0;
            next = 0;
        }

        final Range range = new Range();
        range.length = len;
        range.start = start;
        range.stop = stop;
        range.step = step;
        range.next = next;
        return range;
    }

    protected abstract void setBuffer(final ByteBuffer data);

    protected final ByteBuffer getData() {
        return (ByteBuffer) fData.duplicate().rewind();
    }

    // TODO can probably get rid of this if derived classes pass their Class to
    // the AbstractArray constructor so it can call the right constructor using
    // reflection
    protected abstract E create(int[] dims, int[] strides, ByteBuffer data, int flags, KernelType kernelType);

    protected static int orderAsFlags(final Order order) {
        final Flags fortran;
        if (order.equals(Order.FORTRAN)) {
            fortran = Flags.FORTRAN;
        } else {
            fortran = Flags.EMPTY;
        }
        return fortran.getValue();
    }
}
