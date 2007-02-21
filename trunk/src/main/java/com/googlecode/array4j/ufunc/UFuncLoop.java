package com.googlecode.array4j.ufunc;

public class UFuncLoop {
    private int index = 0;

    private UFunc ufunc;

    private Object[] buffer;

    private Object[] iters;

    private Object[] cast;

    private Object errobj;

    private Object funcdata;

    private Object meth; // ONE_UFUNCLOOP or NOBUFFER_UFUNCLOOP or BUFFER_UFUNCLOOP

    public UFuncLoop(final UFunc ufunc) {
        // construct_loop
        for (int i = 0; i < ufunc.nargs(); i++) {

            // set stuff to null
        }

        // construct arrays
        // loop over actual arguments, which is 2 or 3...
//        if ((nargs < self->nin) || (nargs > self->nargs)) {
//            throw new IllegalArgumentException("invalid number of arguments");
//        }

        /* Get each input argument */
        int nin = 0;
        for (int i = 0; i < nin; i++) {
            // convert to array
            // do some dimensionality checks for scalars and whatnot

        }

        /* Select an appropriate function for these argument types. */

        // TODO array priority stuff

        /*
         * Create copies for some of the arrays if they are small enough and not
         * already contiguous
         */
        // TODO create copies

        /* Create Iterators for the Inputs */
        for (int i = 0; i < nin; i++) {
            /* <CODE>PyArray_IterNew</CODE> */
        }

        /* Broadcast the result */
        /* <CODE>PyArray_Broadcast</CODE> */

        /* Get any return arguments */

        // clear fp flags
    }

    // TODO bufsize
    // TODO errormask
    // TODO errobj
}
