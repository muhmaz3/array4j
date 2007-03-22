package com.googlecode.array4j;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.googlecode.array4j.Indexing.Index;
import com.googlecode.array4j.kernel.Interface;
import com.googlecode.array4j.ufunc.MultiArrayIterator;
import com.googlecode.array4j.ufunc.UFuncs;

// TODO change bitwise operations to use static ints instead of Flags.SOMEFLAG.getValue()

public final class DenseArray implements Array<DenseArray> {
    private static final int ENSURECOPY = Flags.ENSURECOPY.getValue();

    private static final int CONTIGUOUS = Flags.CONTIGUOUS.getValue();

    private static final int ALIGNED = Flags.ALIGNED.getValue();

    private static final int FORTRAN = Flags.FORTRAN.getValue();

    private static final int WRITEABLE = Flags.WRITEABLE.getValue();

    private static final int PSEUDO_INDEX = -1;

    private static final int RUBBER_INDEX = -2;

    private static final int SINGLE_INDEX = -3;

    private static final int MAX_DIMS = 32;

    private static final int BUFSIZE = 10000;

    private int[] fDimensions;

    private int[] fStrides;

    private final ByteBuffer fData;

    private int fFlags;

    private final ArrayDescr fDescr;

    private DenseArray fBase;

    /**
     * Simple constructor.
     * <p>
     * This constructor corresponds to the <CODE>PyArray_SimpleNewFromDescr</CODE>
     * function in NumPy.
     *
     * @param dims
     *            dimensions
     */
    public DenseArray(final ArrayDescr descr, final int[] dims) {
        this(descr, dims, Flags.EMPTY.getValue());
    }

    private DenseArray(final ArrayDescr descr, final int[] dims, final int flags) {
        this(descr, dims, null, null, flags, null);
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
    protected DenseArray(final ArrayDescr descr, final int[] dims, final int[] strides, final ByteBuffer data,
            final int flags, final Object base) {
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
        int sd = itemSize();
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
                sd = itemSize();
            }
            fData = fDescr.createBuffer(sd);
            fFlags |= Flags.OWNDATA.getValue();
        } else {
            fFlags &= ~Flags.OWNDATA.getValue();
            fData = data;
        }
    }

    public static DenseArray fromArray(final DenseArray arr, final ArrayDescr dtype, final int flags) {
        final String msg = "cannot copy back to a read-only array";

        final ArrayDescr oldtype = arr.dtype();
        ArrayDescr newtype;
        if (dtype != null) {
            newtype = dtype;
        } else {
            newtype = oldtype;
        }

        int itemsize = newtype.itemSize();
        if (itemsize == 0) {
            newtype = new ArrayDescr(newtype);
            newtype.setItemSize(oldtype.itemSize());
            itemsize = newtype.itemSize();
        }

        // Can't cast unless ndim-0 array, FORCECAST is specified or the cast is safe.
        if (!Flags.FORCECAST.and(flags) && arr.ndim() != 0 && !oldtype.canCastTo(newtype)) {
            throw new IllegalArgumentException("array cannot be safely cast to required type");
        }

        final DenseArray ret;

        /* Don't copy if sizes are compatible */
        if (Flags.ENSURECOPY.and(flags) || oldtype.isEquivalent(newtype)) {
            final int arrflags = arr.fFlags;
            final boolean copy = ((flags & ENSURECOPY) != 0)
                    || (((flags & CONTIGUOUS) != 0) && ((arrflags & CONTIGUOUS) == 0))
                    || (((flags & ALIGNED) != 0) && ((arrflags & ALIGNED) == 0))
                    || (arr.ndim() > 1 && (((flags & FORTRAN) != 0) && ((arrflags & FORTRAN) == 0)))
                    || (((flags & WRITEABLE) != 0) && ((arrflags & WRITEABLE) == 0));
            if (copy) {
                if (Flags.UPDATEIFCOPY.and(flags) && arr.isWriteable()) {
                    throw new IllegalArgumentException(msg);
                }
                if (Flags.ENSUREARRAY.and(flags)) {
                    // TODO do something with subtype
                }
                // TODO call some constructor
                ret = null;
                arr.copyInto(ret);
                if (Flags.UPDATEIFCOPY.and(flags)) {
                    ret.fFlags |= Flags.UPDATEIFCOPY.getValue();
                    ret.fBase = arr;
                    ret.fBase.fFlags &= ~Flags.WRITEABLE.getValue();
                }
            } else {
                /*
                 * If no copy then just increase the reference count and return
                 * the input.
                 */
                if (Flags.ENSUREARRAY.and(flags) && !arr.checkExact()) {
                    // TODO have to call some derived class's constructor here
                    ret = null;
                    ret.fBase = arr;
                } else {
                    ret = arr;
                }
            }
        } else {
            /*
             * The desired output type is different than the input array type
             * and copy was not specified
             */
            if (Flags.UPDATEIFCOPY.and(flags) && !arr.isWriteable()) {
                throw new IllegalArgumentException(msg);
            }
            if (Flags.ENSUREARRAY.and(flags)) {
                // TODO do something with subtype
            }
            ret = null;
            arr.castTo(ret);
            if (Flags.UPDATEIFCOPY.and(flags)) {
                ret.fFlags |= Flags.UPDATEIFCOPY.getValue();
                ret.fBase = arr;
                ret.fBase.fFlags &= ~Flags.WRITEABLE.getValue();
            }
        }

        return ret;
    }

    /**
     * This method corresponds to the NumPy function <CODE>PyArray_CopyInto</CODE>.
     */
    private void copyInto(final DenseArray ret) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method corresponds to the NumPy function <CODE>PyArray_CastTo</CODE>.
     */
    private void castTo(final DenseArray out) {
        final int mpsize = size();
        if (mpsize == 0) {
            throw new UnsupportedOperationException();
        }
        if (!out.isWriteable()) {
            throw new IllegalArgumentException("output array is not writeable");
        }

        // TODO get cast function

        final boolean same = sameShape(out);
        final boolean simple = same && ((isCArrayRO() && out.isCArray()) || isFArrayRO() && out.isFArray());
        if (simple) {
            // TODO call cast function
        }

        /*
         * If the input or output is STRING, UNICODE, or VOID then getitem and
         * setitem are used for the cast and byteswapping is handled by those
         * methods
         */
        final boolean iswap = isByteSwapped() && !isFlexible();
        final boolean oswap = out.isByteSwapped() && !out.isFlexible();

        // TODO null needs to be castfunc
        broadcastCast(out, null, iswap, oswap);
    }

    private void broadcastCast(final DenseArray out, final Object castfunc, final boolean iswap, final boolean oswap) {
        final int delsize = out.itemSize();
        final int selsize = itemSize();
        final MultiArrayIterator multi = new MultiArrayIterator(2, out, this);
        final ByteBuffer[] buffers = new ByteBuffer[2];

        if (multi.size() != out.size()) {
            throw new IllegalArgumentException("array dimensions are not compatible for copy");
        }

        Object icopyfunc;
        Object ocopyfunc;

        final int n;
        final int maxdim;
        final int ostrides;
        final int istrides;
        final int maxaxis = multi.removeSmallest();
        if (maxaxis < 0) {
            /* cast 1 0-d array to another */
            n = 1;
            maxdim = 1;
            ostrides = delsize;
            istrides = selsize;
        } else {
            maxdim = multi.shape(maxaxis);
            n = Math.min(maxdim, BUFSIZE);
            ostrides = multi.getIterator(0).strides(maxaxis);
            istrides = multi.getIterator(1).strides(maxaxis);
        }
        buffers[0] = Interface.defaultKernel().createBuffer(n * delsize);
        buffers[1] = Interface.defaultKernel().createBuffer(n * selsize);

        for (MultiArrayIterator nextMulti : multi) {
            stridedBufferedCast();
        }
    }

    private void stridedBufferedCast() {
        throw new UnsupportedOperationException();
    }

    /**
     * This method corresponds to the NumPy macro <CODE>PyArray_SAMESHAPE</CODE>.
     */
    private boolean sameShape(final DenseArray arr) {
        return Arrays.equals(fDimensions, arr.fDimensions);
    }

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

    /**
     * Reshape an array.
     * <p>
     * This method corresponds to the NumPy function <CODE>PyArray_Reshape</CODE>.
     */
    public DenseArray reshape(final int... newdims) {
        return reshape(Order.C, newdims);
    }

    /**
     * Returns a new array with the new shape from the data in the old array.
     * <p>
     * This method corresponds to the NumPy function <CODE>PyArray_Newshape</CODE>.
     */
    public DenseArray reshape(final Order order, final int... newdims) {
        final Order fortran = chooseOrder(order);

        /*  Quick check to make sure anything actually needs to be done */
        if (newdims == null || Arrays.equals(fDimensions, newdims)) {
            // TODO might want to return a new object here
            return this;
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
                    strides[0] = itemSize();
                    throw new UnsupportedOperationException();
                }
                for (int i = 1; i < n; i++) {
                    if (strides[i] == 0) {
                        strides[i] = strides[i - 1] * newdims[i - 1];
                    }
                }
            } else {
                if (strides[n - 1] == 0) {
                    strides[n - 1] = itemSize();
                    throw new UnsupportedOperationException();
                }
                for (int i = n - 2; i >= -1; i--) {
                    if (strides[i] == 0) {
                        strides[i] = strides[i + 1] * newdims[i + 1];
                    }
                }
            }
        }

        return new DenseArray(fDescr, newdims, strides, getData(), flags, this);
    }

    public int[] shape() {
        final int[] shapeCopy = new int[fDimensions.length];
        System.arraycopy(fDimensions, 0, shapeCopy, 0, fDimensions.length);
        return shapeCopy;
    }

    public int shape(final int index) {
        return fDimensions[index];
    }

    public int[] strides() {
        final int[] stridesCopy = new int[fStrides.length];
        System.arraycopy(fStrides, 0, stridesCopy, 0, fStrides.length);
        return stridesCopy;
    }

    public int strides(final int index) {
        return fStrides[index];
    }

    public int size() {
        int size = 1;
        for (int dim : fDimensions) {
            // checks in constructor will ensure that this never overflows
            size *= dim;
        }
        return size;
    }

    public DenseArray getArray(final int... indexes) {
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
    private int getOffsetFromIndexes(final int[] indexes) {
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

    private void checkIndexes(final Object... indexes) {
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
    private int indexesToIndex(final int[] indexes) {
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

    private SubIndex parseSubIndex(final Object index, final int max) {
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

    public DenseArray get(final Object... indexes) {
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

        return new DenseArray(fDescr, newdims, newstrides, (ByteBuffer) getDataOffset(offset), fFlags, this);
    }

    public ArrayDescr dtype() {
        return fDescr;
    }

    public int nbytes() {
        return fData.capacity();
    }

    public int ndim() {
        return fDimensions.length;
    }

    public int itemSize() {
        return fDescr.itemSize();
    }

    public int flags() {
        return fFlags;
    }

    public void updateFlags(final Flags... flags) {
        for (final Flags flag : flags) {
        }
    }

    private boolean checkFlags(final Flags... flags) {
        for (final Flags flag : flags) {
            if (!flag.and(fFlags)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns <tt>true</tt> if declaring class of instance is DenseArray.
     * <p>
     * This method corresponds to the NumPy function <CODE>PyArray_CheckExact</CODE>.
     */
    private boolean checkExact() {
        return DenseArray.class.equals(getClass().getDeclaringClass());
    }

    /**
     * This method corresponds to the NumPy macro <CODE>PyArray_ISNUMBER</CODE>.
     */
    public boolean isNumber() {
        return fDescr.type().isNumber();
    }

    public boolean isOneSegment() {
        return ndim() == 0 || Flags.CONTIGUOUS.and(fFlags) || Flags.FORTRAN.and(fFlags);
    }

    public boolean isFortran() {
        return Flags.FORTRAN.and(fFlags) && ndim() > 1;
    }

    public boolean isWriteable() {
        return Flags.WRITEABLE.and(fFlags);
    }

    public boolean isContiguous() {
        return Flags.CONTIGUOUS.and(fFlags);
    }

    public boolean isFArray() {
        return flagSwap(Flags.FARRAY);
    }

    public boolean isFArrayRO() {
        return flagSwap(Flags.FARRAY_RO);
    }

    public boolean isCArray() {
        return flagSwap(Flags.CARRAY);
    }

    public boolean isCArrayRO() {
        return flagSwap(Flags.CARRAY_RO);
    }

    public boolean isNotSwapped() {
        return fDescr.isNativeByteOrder();
    }

    public boolean isByteSwapped() {
        return !isNotSwapped();
    }

    /**
     * This method corresponds to the NumPy macro <CODE>PyArray_ISFLEXIBLE</CODE>.
     */
    public boolean isFlexible() {
        return fDescr.type().isFlexible();
    }

    public boolean isBehaved() {
        return flagSwap(Flags.BEHAVED);
    }

    public boolean isBehavedRo() {
        return flagSwap(Flags.ALIGNED);
    }

    private boolean flagSwap(final Flags flags) {
        return flags.and(fFlags) && isNotSwapped();
    }

    public DenseArray addEquals(final Array<?> arr) {
        if (!(arr instanceof DenseArray)) {
            throw new UnsupportedOperationException();
        }
        UFuncs.ADD.call(this, (DenseArray) arr, this);
        return this;
    }

    public DenseArray multiplyEquals(final Array<?> arr) {
        if (!(arr instanceof DenseArray)) {
            throw new UnsupportedOperationException();
        }
        UFuncs.MULTIPLY.call(this, (DenseArray) arr, this);
        return this;
    }

    public DenseArray squareEquals() {
        UFuncs.SQUARE.call(this, this);
        return this;
    }

    public DenseArray sqrtEquals() {
        UFuncs.SQRT.call(this, this);
        return this;
    }

    public DenseArray ldexpEquals(final Array<?> arr) {
        if (!(arr instanceof DenseArray)) {
            throw new UnsupportedOperationException();
        }
        UFuncs.LDEXP.call(this, (DenseArray) arr, this);
        return this;
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

    public ByteBuffer getData() {
        return (ByteBuffer) fData.duplicate().rewind();
    }

    public ByteBuffer getDataOffset(final int offset) {
        return (ByteBuffer) getData().position(offset);
    }

    protected static int orderAsFlags(final Order order) {
        final Flags fortran;
        if (order.equals(Order.FORTRAN)) {
            fortran = Flags.FORTRAN;
        } else {
            fortran = Flags.EMPTY;
        }
        return fortran.getValue();
    }

    @Override
    public String toString() {
        // TODO look at arrayprint.py in NumPy for some ideas
        throw new UnsupportedOperationException();
    }

    public double getDouble(final int... indexes) {
        // TODO byte swapping and all that
        return getData().getDouble(getOffsetFromIndexes(indexes));
    }

    public int getInteger(final int... indexes) {
        // TODO byte swapping and all that
        return getData().getInt(getOffsetFromIndexes(indexes));
    }
}
