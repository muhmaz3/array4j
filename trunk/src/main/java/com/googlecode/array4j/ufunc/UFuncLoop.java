package com.googlecode.array4j.ufunc;

import java.util.Arrays;

import com.googlecode.array4j.Array2;

public class UFuncLoop {
    private enum LoopMethod {
        NO_UFUNCLOOP,
        ONE_UFUNCLOOP,
        BUFFER_UFUNCLOOP;
    }

    private int fIndex;

    private UFunc fUfunc;

    private Object[] fIters;

    private Object[] fCast;

    private Object fErrObj;

    private boolean fNotImplemented;

    private boolean fFirst;

    private int[] fDimensions;

    private int fBufCnt;

    private LoopMethod fMeth;

    private boolean[] fNeedBuffer;

    // TODO should be able to get this value from fIters.length
    private int fNumIter;

    private int[] fSteps;

    public UFuncLoop(final UFunc ufunc, final Array2<?>[] args) {
        // TODO support something like NumPy's extobj and sig keyword arguments
        this.fIndex = 0;
        this.fUfunc = ufunc;
        // TODO buffer
        this.fIters = new Object[ufunc.nargs()];
        this.fCast = new Object[ufunc.nargs()];
        this.fNotImplemented = false;
        this.fFirst = true;
        constructArrays(args);
    }

    private int constructArrays(final Array2<?>[] args) {
        final Array2<?>[] mps = new Array2<?>[0];

        /* Check number of arguments */
        final int nargs = args.length;
        final int nin = nin();
        if (nargs < nin || nargs > nargs()) {
            throw new IllegalArgumentException("invalid number of arguments");
        }

        /* Get each input argument */
        boolean flexible = false;
        boolean object = false;
        for (int i = 0; i < nin; i++) {
            mps[i] = args[i];

            // TODO check for flexible and object arrays
            // TODO various array scalar bits
        }

        if (flexible && !object) {
            fNotImplemented = true;
            return nargs();
        }

        /* If everything is a scalar, then use normal coercion rules */
        if (false) {
            // TODO look at supporting array scalars
        }

        /* Select an appropriate function for these argument types. */
        selectTypes();

        /*
         * Create copies for some of the arrays if they are small enough and not
         * already contiguous
         */
        createCopies();

        /* Create Iterators for the Inputs */
        for (int i = 0; i < nin; i++) {
            fIters[i] = new ArrayIterator(mps[i]);
        }

        /* Broadcast the result */
        broadcast(null);

        /* Get any return arguments */
        for (int i = nin; i < nargs; i++) {
            mps[i] = args[i];
            final int nd = fDimensions.length;
            if (mps[i].ndim() != nd || !Arrays.equals(mps[i].shape(), fDimensions)) {
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

            /* still not the same -- or will we have to use buffers?*/
            if (false) {
            }

            fIters[i] = new ArrayIterator(mps[i]);
        }

        /*
         * If any of different type, or misaligned or swapped then must use
         * buffers
         */

        // TODO array4j specific: also use buffers if kernel types don't match
        // up, since native code requires direct buffers

        fBufCnt = 0;
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

        // TODO
        fNumIter = nargs;

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
            final int nd = fDimensions.length;
            final int[] stridesum = new int[0];
            for (int i = 0; i < nd; i++) {
                for (int j = 0; j < fNumIter; j++) {
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
            final int maxdim = fDimensions[ldim];
//            fSize /= maxdim;
//            fBufCnt = maxdim;
//            fLastDim = ldim;

            /*
             * Fix the iterators so the inner loop occurs over the largest
             * dimensions -- This can be done by setting the size to 1 in that
             * dimension (just in the iterators).
             */
            for (int i = 0; i < fNumIter; i++) {
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

    private void selectTypes() {
    }

    private void createCopies() {
    }

    public final int nin() {
        return fUfunc.nin();
    }

    public final int nout() {
        return fUfunc.nout();
    }

    public final int nargs() {
        return fUfunc.nargs();
    }

    public static void broadcast(final MultiArrayIterator mit) {
        // TODO broadcast sets nd of loop
    }
}
