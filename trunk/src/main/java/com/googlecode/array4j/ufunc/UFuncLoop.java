package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;

import com.googlecode.array4j.ArrayDescr;
import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.Flags;
import com.googlecode.array4j.ScalarKind;

public final class UFuncLoop implements Iterable<MultiArrayIterator> {
    private enum LoopMethod {
        NO_UFUNCLOOP,
        ONE_UFUNCLOOP,
        NOBUFFER_UFUNCLOOP,
        BUFFER_UFUNCLOOP;
    }

    private int fIndex;

    private final UFunc fUfunc;

    /* Buffers for the loop */
    private final ByteBuffer[] fBuffer;

    private final MultiArrayIterator mit;

    private final Object[] fCast;

    /* The loop caused notimplemented */
    private boolean fNotImplemented;

    private boolean fFirst;

    private final int fBufSize;

    private final int fErrorMask;

    private final ErrorHandler fErrObj;

    private final boolean[] fNeedBuffer;

    private final int[] fSteps;

    private LoopMethod fMeth;

    public UFuncLoop(final UFunc ufunc, final DenseArray[] args) {
        this(ufunc, args, Error.DEFAULT_ERROR);
    }

    public UFuncLoop(final UFunc ufunc, final DenseArray[] args, final Error extobj) {
        this.fIndex = 0;
        this.fUfunc = ufunc;
        final int nargs = nargs();
        this.fBuffer = new ByteBuffer[nargs];
        fBuffer[0] = null;

        // the multi iterator contains the iterators
        this.mit = new MultiArrayIterator(nargs);
        this.fCast = new Object[nargs];
        // TODO fCast contains PyArray_VectorUnaryFuncs

        this.fNotImplemented = false;
        this.fFirst = true;

        this.fBufSize = extobj.getBufSize();
        this.fErrorMask = extobj.getErrorMask();
        this.fErrObj = extobj.getErrorHandler();

        // initialize other arrays (these have a static size in NumPy)
        this.fNeedBuffer = new boolean[nargs];
        this.fSteps = new int[nargs];

        constructArrays(args);
    }

    public void execute() {
        if (fNotImplemented) {
            throw new UnsupportedOperationException();
        }

        switch (fMeth) {
        case ONE_UFUNCLOOP:
            throw new UnsupportedOperationException();
//            break;
        case NOBUFFER_UFUNCLOOP:
            for (MultiArrayIterator it : this) {
                // TODO update bufptr from iter
                // TODO call function
            }
            throw new UnsupportedOperationException();
//            break;
        case BUFFER_UFUNCLOOP:
            throw new UnsupportedOperationException();
//            break;
        default:
            throw new AssertionError();
        }
    }

    public Iterator<MultiArrayIterator> iterator() {
        return mit;
    }

    private int constructArrays(final DenseArray[] args) {
        // TODO if input and output arrays don't all have dtypes with the same
        // kernel (i. e. the same type of underlying buffer), use some kind of
        // heuristic to decide if we should copy the Java arrays or the C arrays
        // and call Java code or C code

        /* Check number of arguments */
        final int nargs = args.length;
        final int nin = nin();
        if (nargs < nin || nargs > nargs()) {
            throw new IllegalArgumentException("invalid number of arguments");
        }

        final DenseArray[] mps = new DenseArray[nargs()];
        final ArrayType[] argtypes = new ArrayType[mps.length];
        final ScalarKind[] scalars = new ScalarKind[mps.length];
        boolean allscalars = true;

        /* Get each input argument */
        boolean flexible = false;
        boolean object = false;
        for (int i = 0; i < nin; i++) {
            if (false) {
            } else {
                // TODO set context to null
            }
            // TODO mps[i] = PyArray_FromAny(obj, NULL, 0, 0, 0, context)

            mps[i] = args[i];
            argtypes[i] = mps[i].dtype().type();
            if (!flexible && argtypes[i].isFlexible()) {
                flexible = true;
            }
            if (!object && argtypes[i].isObject()) {
                object = true;
            }

            /* Scalars are 0-dimensional arrays at this point */
            if (mps[i].ndim() > 0) {
                scalars[i] = ScalarKind.NOSCALAR;
                allscalars = false;
            }
        }

        if (flexible && !object) {
            fNotImplemented = true;
            return nargs();
        }

        /* If everything is a scalar, then use normal coercion rules */
        if (allscalars) {
            for (int i = 0; i < nin; i++) {
                scalars[i] = ScalarKind.NOSCALAR;
            }
        }

        /* Select an appropriate function for these argument types. */
        final Object typetup = null;
        fUfunc.selectTypes(argtypes, scalars, typetup);

        if (argtypes[1].equals(ArrayType.OBJECT) && nin() == 2 && nout() == 1) {
            throw new UnsupportedOperationException();
        }

        /*
         * Create copies for some of the arrays if they are small enough and not
         * already contiguous.
         */
        createCopies(argtypes, mps);

        /* Create Iterators for the Inputs */
        for (int i = 0; i < nin; i++) {
            mit.createArrayIterator(i, mps[i]);
        }

        /* Broadcast the result over the input arguments. */
        mit.broadcast(nin());

        /* Get any return arguments */
        for (int i = nin; i < nargs; i++) {
            mps[i] = args[i];
            final int nd = mit.ndim();
            if (mps[i].ndim() != nd || !Arrays.equals(mps[i].shape(), mit.shape())) {
                throw new IllegalArgumentException("invalid return array shape");
            }
            if (!mps[i].isWriteable()) {
                throw new IllegalArgumentException("return array is not writeable");
            }
        }

        /* construct any missing return arrays and make output iterators */
        for (int i = nin; i < nargs; i++) {
            if (mps[i] == null) {
                // TODO create a new array
            } else {
                /*
                 * reset types for outputs that are equivalent -- no sense
                 * casting uselessly
                 */

            }

            /* still not the same -- or will we have to use buffers? */
            if (false) {
            }

            mit.createArrayIterator(i, mps[i]);
        }

        /*
         * If any of different type, or misaligned or swapped then must use
         * buffers
         */

//        fBufCnt = 0;
//        obj = false

        /* Determine looping method needed */
        fMeth = LoopMethod.NO_UFUNCLOOP;

        // TODO
//        if (fSize == 0) {
//            return nargs;
//        }

        for (int i = 0; i < nargs; i++) {
            fNeedBuffer[i] = false;
            if (false) {
                fMeth = LoopMethod.BUFFER_UFUNCLOOP;
                fNeedBuffer[i] = true;
            }
            if (false) {
//                obj = true;
            }
        }

        if (fMeth.equals(LoopMethod.NO_UFUNCLOOP)) {
            fMeth = LoopMethod.ONE_UFUNCLOOP;

            /* All correct type and BEHAVED */
            /* Check for non-uniform stridedness */
            for (int i = 0; i < nargs; i++) {

            }
            if (fMeth.equals(LoopMethod.ONE_UFUNCLOOP)) {
                for (int i = 0; i < nargs; i++) {
                    // TODO set bufptr[i] to mps[i].data()
                }
            }
        }

        // TODO more code here?
        // TODO this bit is probably unnecessary
//        fNumIter = nargs;

        if (!fMeth.equals(LoopMethod.ONE_UFUNCLOOP)) {
            /* Fix iterators */

            /*
             * Optimize axis the iteration takes place over.
             *
             * The first thought was to have the loop go over the largest
             * dimension to minimize the number of loops.
             *
             * However, on processors with slow memory bus and cache, the
             * slowest loops occur when the memory access occurs for large
             * strides.
             *
             * Thus, choose the axis for which strides of the last iterator is
             * smallest but non-zero.
             */
            final int nd = mit.ndim();
            final int[] stridesum = new int[0];
            for (int i = 0; i < nd; i++) {
                for (int j = 0; j < mit.numiter(); j++) {
//                    stridesum[i] += fIters[j].strides(i);
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
            final int maxdim = mit.shape(ldim);
//            fSize /= maxdim;
//            fBufCnt = maxdim;
//            fLastDim = ldim;

            /*
             * Fix the iterators so the inner loop occurs over the largest
             * dimensions -- This can be done by setting the size to 1 in that
             * dimension (just in the iterators).
             */
            for (int i = 0; i < mit.numiter(); i++) {
            }

            /*
             * fix up steps where we will be copying data to buffers and
             * calculate the ninnerloops and leftover values -- if step size is
             * already zero that is not changed...
             */
            if (fMeth.equals(LoopMethod.BUFFER_UFUNCLOOP)) {

            }
        } else {
            /* uniformly-strided case ONE_UFUNCLOOP */
            for (int i = 0; i < nargs; i++) {
                if (mps[i].size() == 1) {
                    fSteps[i] = 0;
                } else {
                    fSteps[i] = mps[i].strides(mps[i].ndim() - 1);
                }
            }
        }

        /* Finally, create memory for buffers if we need them */

        /*
         * buffers for scalars are specially made small -- scalars are not
         * copied multiple times
         */
        if (fMeth.equals(LoopMethod.BUFFER_UFUNCLOOP)) {
            // TODO more stuff to do here
        }

        return nargs;
    }

    private void createCopies(final ArrayType[] argtypes, final DenseArray[] mps) {
        final int nin = nin();
        for (int i = 0; i < nin; i++) {
            final int size = mps[i].size();
            /*
             * if the type of mps[i] is equivalent to argtypes[i] then set
             * arg_types[i] equal to type of mps[i] for later checking....
             */
            if (!mps[i].dtype().type().equals(argtypes[i])) {
                final ArrayDescr ntype = mps[i].dtype();
                final ArrayDescr atype = ArrayDescr.fromType(argtypes[i]);
                if (atype.isEquivalent(ntype)) {
                    argtypes[i] = ntype.type();
                }
            }
            if (size < fBufSize) {
                if (!mps[i].isBehavedRo() || mps[i].dtype().type() != argtypes[i]) {
                    final ArrayDescr ntype = mps[i].dtype();
                    // NumPy code calls PyArray_FromAny which calls
                    // PyArray_FromArray, which corresponds to the constructor
                    // called here
                    mps[i] = new DenseArray(mps[i], ntype, Flags.or(Flags.FORCECAST, Flags.ALIGNED));
                }
            }
        }
    }

    public int nin() {
        return fUfunc.nin();
    }

    public int nout() {
        return fUfunc.nout();
    }

    public int nargs() {
        return fUfunc.nargs();
    }
}
