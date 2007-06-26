package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.DenseArray;

/**
 * This class corresponds to <CODE>PyUFuncReduceObject</CODE> in NumPy.
 */
public final class UFuncReduce {
    private enum LoopMethod {
        ZERO_EL_REDUCELOOP,
        ONE_EL_REDUCELOOP,
        NOBUFFER_REDUCELOOP,
        BUFFER_REDUCELOOP
    }

    private ArrayIterator it;

    private DenseArray ret;

    /* Needed for Accumulate */
    private ArrayIterator rit;

    private int outsize;

    private int[] index;

    private int size;

    private Object[] idptr;

    private UFunc ufunc;

    private int errormask;

    private Object errobj;

    private int first;

    private Object function;

    private Object funcdata;

    private LoopMethod meth;

    private boolean swap;

    private ByteBuffer buffer;

    private int bufsize;

    private Object castbuf;

    private Object cast;

    private Object bufptr;

    private int[] steps;

    private int N;

    private int instrides;

    private int insize;

    private Object inptr;

    private boolean obj;

    private boolean retbase;

    public UFuncReduce() {
        constructReduce();
    }

    private void constructReduce() {
    }
}
