package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayDescr;
import com.googlecode.array4j.ArrayFunctions;
import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.ScalarKind;

/**
 * This class corresponds to the <CODE>PyUFuncObject</CODE> struct in NumPy.
 */
public abstract class AbstractUFunc implements UFunc {
    protected enum Identity {
        ZERO,
        ONE,
        NONE;
    }

    protected static final class Signature {
        private final ArrayType[] fin;

        private final ArrayType[] fout;

        private final ArrayFunctions funcs;

        public Signature(final ArrayType[] in, final ArrayType[] out, final ArrayFunctions funcs) {
            this.fin = in;
            this.fout = out;
            this.funcs = funcs;
        }

        public ArrayFunctions getFunctions() {
            return funcs;
        }
    }

    private final int fNin;

    private final int fNout;

    private final Identity fIdentity;

    private final Object[] fData;

//    private final boolean checkReturn;

    private final Signature[] fTypes;

//    private Object ptr;

//    private Object obj;

//    private Object[] userloops;

    /**
     * <p>
     * This constructors corresponds to the NumPy function <CODE>PyUFunc_FromFuncAndData</CODE>.
     */
    protected AbstractUFunc(final Object[] data, final Signature[] signatures, final int nin, final int nout,
            final Identity identity) {
        // TODO maybe make AbstractUFunc generic so that we can specify the
        // type of elements in data
        this.fData = data;
        this.fTypes = signatures;
        this.fNin = nin;
        this.fNout = nout;
        this.fIdentity = identity;
    }

    public final int nin() {
        return fNin;
    }

    public final int nout() {
        return fNout;
    }

    public final int nargs() {
        return fNin + fNout;
    }

    /**
     * Called to determine coercion. Can change argtypes.
     */
    public final Object[] selectTypes(final ArrayType[] argtypes, final ScalarKind[] scalars, final Object typetup) {
        int userdef = -1;
        if (false) {
            // TODO check if ufunc has a user loop
        }

        if (typetup != null) {
            // TODO handle typetyp... something to do with the sig kwarg
            throw new UnsupportedOperationException();
        }

        if (userdef > 0) {
            throw new UnsupportedOperationException();
        }

        ArrayType starttype = argtypes[0];
        /*
         * If the first argument is a scalar we need to place the start type as
         * the lowest type in the class
         */
        if (scalars[0] != ScalarKind.NOSCALAR) {
            starttype = lowestType(starttype);
        }

        Signature selectedSig = null;
        for (Signature sig : fTypes) {
            if (starttype.compareTo(sig.fin[0]) > 0) {
                continue;
            } else {
                boolean canCoerceAll = true;
                for (int j = 0; j < nin(); j++) {
                    if (!argtypes[j].canCoerceScalar(sig.fin[j], scalars[j])) {
                        canCoerceAll = false;
                        break;
                    }
                }
                if (canCoerceAll) {
                    selectedSig = sig;
                    break;
                }
            }
        }
        if (selectedSig == null) {
            throw new IllegalArgumentException("function not supported for these types, "
                    + "and can't coerce safely to supported types");
        }

        for (int j = 0; j < nin(); j++) {
            argtypes[j] = selectedSig.fin[j];
        }
        for (int j = nin(); j < nargs(); j++) {
            argtypes[j] = selectedSig.fout[j - nin()];
        }


        final Object[] func = new Object[2];
        // TODO need to choose between java functions and native functions here
        func[0] = selectedSig.getFunctions();
        // TODO set function data if there is any
        func[1] = null;
        return func;
    }

    private static ArrayType lowestType(final ArrayType intype) {
        switch(intype) {
        case SHORT:
        case INT:
        case LONG:
            return ArrayType.BYTE;
        case DOUBLE:
            return ArrayType.FLOAT;
        case CDOUBLE:
            return ArrayType.CFLOAT;
        default:
            return intype;
        }
    }

    public abstract void call(ArrayFunctions functions, ByteBuffer[] bufptr, int[] dimensions, int[] steps,
            Object funcdata);

    /**
     * Generic ufunc call.
     * <p>
     * This function corresponds to the NumPy functions <CODE>ufunc_generic_call</CODE>
     * and <CODE>PyUFunc_GenericFunction</CODE>.
     */
    public final DenseArray[] call(final DenseArray... args) {
        final UFuncLoop loop = new UFuncLoop(this, args);
        return loop.execute();
    }

    private void genericReduction(final DenseArray arr, final int axis, final ArrayDescr otype) {
        if (nin() != 2) {
            throw new UnsupportedOperationException("only supported for binary functions");
        }
        if (nout() != 1) {
            throw new UnsupportedOperationException("only supported for functions returning a single value");
        }

        /* Check to see if input is zero-dimensional */
        if (arr.ndim() == 0) {
            throw new IllegalArgumentException("cannot operate on a scalar");
        }

        /* Check to see that type (and otype) is not FLEXIBLE */
        if (arr.isFlexible() || (otype != null && otype.type().isFlexible())) {
            // TODO need another flexibility check here
            throw new IllegalArgumentException("cannot perform operation with flexible type");
        }

        int newaxis = axis;
        if (newaxis < 0) {
            newaxis += arr.ndim();
        }
        if (newaxis < 0 || newaxis >= arr.ndim()) {
            throw new IllegalArgumentException("axis not in array");
        }

//        if (otype == NULL && out != NULL) {
//            otype = out->descr;
//            Py_INCREF(otype);
//        }

        if (otype == null) {
            /*
             * For integer types --- make sure at least a long is used for add
             * and multiply reduction --- to avoid overflow
             */
            ArrayType typenum = arr.dtype().type();
            if (typenum.compareTo(ArrayType.FLOAT) < 0 && false) {
                if (typenum.isBool()) {
                    typenum = ArrayType.INT;
                } else if (arr.dtype().itemSize() < 0) {
                    if (typenum.isUnsigned()) {
                        throw new UnsupportedOperationException();
                    } else {
                        typenum  = ArrayType.INT;
                    }
                }
                // TODO need to assign this dtype to something
//                otype = ArrayDescr.fromType(typenum);
            }
        }
    }

    public final DenseArray reduce(final DenseArray arr, final int axis, final DenseArray out, final ArrayDescr otype) {
        genericReduction(arr, axis, otype);
        return null;
    }

    /**
     * This method corresponds to the NumPy function <CODE>PyUFunc_Accumulate</CODE>.
     */
    public final DenseArray accumulate(final DenseArray arr, final int axis, final DenseArray out,
            final ArrayDescr otype) {
        genericReduction(arr, axis, otype);
        final UFuncReduce loop = new UFuncReduce();
        // TODO swithc on loop method and do something
        return out;
    }

    public final DenseArray reduceat(final DenseArray arr, final int axis, final int[] indices, final DenseArray out,
            final ArrayDescr otype) {
        genericReduction(arr, axis, otype);
        return null;
    }

    /**
     * This method corresponds to the function <CODE>ufunc_outer</CODE> in NumPy.
     */
    public final DenseArray outer(final DenseArray arr1, final DenseArray arr2) {
        if (nin() != 2) {
            throw new UnsupportedOperationException("outer product only supported for binary functions");
        }
        // TODO make sure this is a copy
        final int[] newshape = new int[arr1.ndim() + arr2.ndim()];
        System.arraycopy(arr1.shape(), 0, newshape, 0, arr1.ndim());
        System.arraycopy(arr2.shape(), 0, newshape, arr1.ndim(), arr2.ndim());
        final DenseArray arrnew = arr1.reshape(newshape);
        // TODO find out if any ufunc outers return more than one array
        return call(arrnew, arr2)[0];
    }
}
