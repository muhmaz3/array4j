package com.googlecode.array4j.ufunc;

import java.nio.ByteBuffer;

import com.googlecode.array4j.ArrayDescr;
import com.googlecode.array4j.ArrayFunctions;
import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.ScalarKind;

// TODO allow functions to specify extobj and sig arguments

public interface UFunc {
    int nin();

    int nout();

    int nargs();

    Object[] selectTypes(ArrayType[] argtypes, ScalarKind[] scalars, Object typetup);

    void call(ArrayFunctions functions, ByteBuffer[] bufptr, int[] dimensions, int[] steps, Object funcdata);

    DenseArray[] call(DenseArray... args);

    DenseArray reduce(DenseArray arr, int axis, DenseArray out, ArrayDescr otype);

    DenseArray accumulate(DenseArray arr, int axis, DenseArray out, ArrayDescr otype);

    DenseArray reduceat(DenseArray arr, int axis, int[] indices, DenseArray out, ArrayDescr otype);

    DenseArray outer(DenseArray arr1, DenseArray arr2);
}
