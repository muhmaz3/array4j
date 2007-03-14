package com.googlecode.array4j;

/**
 * Array range.
 * <p>
 * This code corresponds to the NumPy function <CODE>PyArray_ArangeObj</CODE>.
 */
final class Arange {
    private Arange() {
    }

    public static DenseArray arange(final Object start, final Object stop, final Object step, final ArrayDescr dtype) {
        final ArrayDescr nonNullDtype;
        if (dtype == null) {
            ArrayDescr deftype = ArrayDescr.fromType(ArrayType.INT);
            ArrayDescr newtype = ArrayDescr.fromObject(start, deftype);
            deftype = newtype;
            if (stop != null) {
                newtype = ArrayDescr.fromObject(stop, deftype);
                deftype = newtype;
            }
            if (step != null) {
                newtype = ArrayDescr.fromObject(step, deftype);
                deftype = newtype;
            }
            nonNullDtype = deftype;
        } else {
            nonNullDtype = dtype;
        }

        final Object nonNullStep = step != null ? step : Integer.valueOf(1);
        final Object nonNullStop;
        final Object nonNullStart;
        if (stop == null) {
            nonNullStop = start;
            nonNullStart = Integer.valueOf(0);
        } else {
            nonNullStop = stop;
            nonNullStart = start;
        }

        /* calculate the length and next = start + step*/
        final Object[] next = new Number[1];
        int length = calcLength(nonNullStart, nonNullStop, nonNullStep, next, dtype.getType().isComplex());

        if (length <= 0) {
            length = 0;
            return new DenseArray(dtype, new int[]{length});
        }

        /*
         * If dtype is not in native byte-order then get native-byte order
         * version. And then swap on the way out.
         */
        if (!nonNullDtype.isNativeByteOrder()) {
            throw new UnsupportedOperationException();
        }

        // TODO construct with dtype that has native byte order
        final DenseArray range = new DenseArray(dtype, new int[]{length});

        final ArrayFunctions funcs = range.dtype().getArrayFunctions();

        /* place start in the buffer and the next value in the second position */
        /* if length > 2, then call the inner loop, otherwise stop */

        funcs.setitem(nonNullStart, range.getData(), range);
        if (length == 1) {
            return range;
        }
        funcs.setitem(next[0], range.getDataOffset(range.itemSize()), range);
        if (length == 2) {
            return range;
        }
        funcs.fill(range.getData(), length);

        if (false) {
            // TODO swap array's byte order before returning
        }

        return range;
    }

    private static int calcLength(final Object start, final Object stop, final Object step, final Object[] next,
            final boolean complex) {
        next[0] = subtract(stop, start);
        final Object val = trueDivide(next[0], step);
        final int len;
        if (complex && isComplex(val)) {
            throw new UnsupportedOperationException();
        } else {
            final double value = ((Number) val).doubleValue();
            len = (int) Math.ceil(value);
        }

        if (len > 0) {
            next[0] = add(start, step);
        }

        return len;
    }

    private static Object add(final Object op1, final Object op2) {
        if (op1 instanceof Number && op2 instanceof Number) {
            return Double.valueOf(((Number) op1).doubleValue() + ((Number) op2).doubleValue());
        }
        throw new UnsupportedOperationException();
    }

    private static Object subtract(final Object op1, final Object op2) {
        if (op1 instanceof Number && op2 instanceof Number) {
            return Double.valueOf(((Number) op1).doubleValue() - ((Number) op2).doubleValue());
        }
        throw new UnsupportedOperationException();
    }

    private static Object trueDivide(final Object op1, final Object op2) {
        if (isComplex(op1) || isComplex(op2)) {
            throw new UnsupportedOperationException();
        } else {
            final Number num1 = (Number) op1;
            final Number num2 = (Number) op2;
            return Double.valueOf(num1.doubleValue() / num2.doubleValue());
        }
    }

    private static boolean isComplex(final Object obj) {
        return obj instanceof ComplexDouble || obj instanceof ComplexFloat;
    }
}
