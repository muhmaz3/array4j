package net.lunglet.fft;

import java.nio.FloatBuffer;

import net.lunglet.mkl.fft.DftiConfigParam;
import net.lunglet.mkl.fft.DftiConfigValue;
import net.lunglet.mkl.fft.DftiDescriptor;
import net.lunglet.mkl.fft.DftiException;

import com.googlecode.array4j.dense.CFloatDenseVector;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class MKLFFT implements FFT {
    private static void checkArgument(final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public CFloatDenseVector fft(final FloatDenseVector x, final int n) {
        // TODO short circuit on zero-length x instead of throwing
        checkArgument(x.size() > 0 && n > 0);
        checkArgument(x.isDirect());

        final FloatBuffer xdata;
        final int xoffset;
        if (n > x.size()) {
            xdata = FloatDenseVector.createFloatBuffer(n, x.storage());
            xoffset = 0;
            // TODO optimize this
            for (int i = 0; i < x.size(); i++) {
                xdata.put(x.get(i));
            }
        } else {
            xdata = x.data();
            xoffset = x.offset();
        }
        try {
            final int[] lengths = new int[]{n};
            DftiDescriptor desc = new DftiDescriptor(DftiConfigValue.SINGLE, DftiConfigValue.REAL, lengths);
            try {
                desc.setValue(DftiConfigParam.FORWARD_SCALE, 1.0f);
                desc.setValue(DftiConfigParam.BACKWARD_SCALE, 1.0f / lengths[0]);
                desc.setValue(DftiConfigParam.PLACEMENT, DftiConfigValue.NOT_INPLACE);
                desc.commit();
                // TODO call a zerosLike function here
                // TODO need to do a few things to the output data to get complete transform
                CFloatDenseVector y = new CFloatDenseVector(n, x.orientation(), x.storage());
                desc.computeForward(xdata, xoffset, y.data(), y.offset());
                return y;
            } finally {
                desc.free();
            }
        } catch (DftiException e) {
            throw new RuntimeException(e);
        }
    }
}
