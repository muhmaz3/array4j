package com.googlecode.array4j.fft;

import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Map;

import com.googlecode.array4j.ArrayType;
import com.googlecode.array4j.DenseArray;

public final class FFT {
    static {
        System.loadLibrary("array4j");
    }

    private static final Map<Integer, DenseArray> FFT_CACHE = new Hashtable<Integer, DenseArray>();

    private FFT() {
    }

    public static DenseArray fft(final DenseArray arr, final int n, final int axis) {
        return rawfft(arr, n, axis);
    }

    private static DenseArray rawfft(final DenseArray a, final int n, final int axis) {
        if (n < 1) {
            throw new IllegalArgumentException("invalid number of FFT data points (" + n + ") specified");
        }
        final DenseArray wsave = getWorkFromCache(n);

        if (a.shape(axis) != n) {
            throw new UnsupportedOperationException();
        }

        final DenseArray b;
        if (axis != -1) {
            b = a.swapaxes(axis, -1);
        } else {
            b = a;
        }

        // TODO support other work functions
        final DenseArray r = cfftf(b, wsave);

        if (axis != -1) {
            return r.swapaxes(axis, -1);
        } else {
            return r;
        }
    }

    private static DenseArray cfftf(final DenseArray op1, final DenseArray op2) {
        final DenseArray data = op1.copy(ArrayType.CDOUBLE);

        //  TODO nsave should contain number of elements in wsave
        final int nsave = 0;
        final ByteBuffer wsave = op2.as1d(ArrayType.DOUBLE);

        final int npts = data.shape(data.ndim() - 1);
        if (nsave != npts * 4 + 15) {
            throw new AssertionError("invalid work array for fft size");
        }

        final int nrepeats = data.size() / npts;

        ByteBuffer dptr = data.getData();
        for (int i = 0; i < nrepeats; i++) {
            cfftf(npts, dptr, dptr.position(), wsave);
            dptr = data.getDataOffset(npts * 2);
        }

        return data;
    }

    private static native void cfftf(int npts, ByteBuffer c, int offset, ByteBuffer wsave);

    private static DenseArray getWorkFromCache(final int n) {
        // TODO need some scheme here to expire the LRU cache entries if cache
        // gets too big
        if (FFT_CACHE.containsKey(n)) {
            return FFT_CACHE.get(n);
        }
        // TODO support multiple init functions here
        /* Magic size needed by cffti */
        final int dim = 4 * n + 15;
        final DenseArray wsave = new DenseArray(ArrayType.DOUBLE, dim);
        cffti(n, wsave.getData());
        FFT_CACHE.put(n, wsave);
        return wsave;
    }

    private static native void cffti(int n, ByteBuffer data);
}
