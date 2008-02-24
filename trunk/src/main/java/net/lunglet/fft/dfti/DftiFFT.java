package net.lunglet.fft.dfti;

import com.googlecode.array4j.matrix.dense.CFloatDenseVector;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.nio.FloatBuffer;
import net.lunglet.fft.FFT;
import net.lunglet.util.AssertUtils;
import net.lunglet.util.BufferUtils;

public final class DftiFFT implements FFT {
    @Override
    public CFloatDenseVector fft(final FloatDenseVector x, final int n) {
        // TODO short circuit on zero-length x instead of throwing
        AssertUtils.checkArgument(x.length() > 0 && n > 0);
        // TODO JNA should convert all the arguments to native arrays
//        checkArgument(x.isDirect());

        final FloatBuffer xdata;
        final int xoffset;
        final int xstride;
        if (n > x.length()) {
            xdata = BufferUtils.createFloatBuffer(n, x.storage());
            xoffset = 0;
            xstride = 1;
            // TODO optimize this
            for (int i = 0; i < x.length(); i++) {
                xdata.put(x.get(i));
            }
        } else {
//            xdata = x.data();
            xdata = null;
            xoffset = x.offset();
            xstride = x.stride();
        }
        try {
            final int[] lengths = new int[]{n};
            DftiDescriptor desc = new DftiDescriptor(DftiConfigValue.SINGLE, DftiConfigValue.REAL, lengths);
            try {
                // TODO call a zerosLike function here
//                CFloatDenseVector y = new CFloatDenseVector(n, x.order(), x.storage());
                CFloatDenseVector y = null;
                desc.setValue(DftiConfigParam.FORWARD_SCALE, 1.0f);
                desc.setValue(DftiConfigParam.BACKWARD_SCALE, 1.0f / lengths[0]);
                desc.setValue(DftiConfigParam.PLACEMENT, DftiConfigValue.NOT_INPLACE);
                desc.setValue(DftiConfigParam.INPUT_STRIDES, new int[]{xoffset, xstride});
                desc.setValue(DftiConfigParam.OUTPUT_STRIDES, new int[]{y.offset(), y.stride()});
                desc.commit();
                desc.computeForward(xdata, y.data());
                if (n % 2 == 0) {
                    for (int i = 1; i <= n / 2 - 1; i++) {
                        y.set(n / 2 + i, y.get(n / 2 - i).conj());
                    }
                } else {
                    int m = n / 2;
                    for (int i = 1; i <= m; i++) {
                        y.set(m + i, y.get(m - i + 1).conj());
                    }
                }
                return y;
            } finally {
                desc.free();
            }
        } catch (DftiException e) {
            throw new RuntimeException(e);
        }
    }
}
