package net.lunglet.svm;

final class SVRKernel extends Kernel {
    private final int l;

    private final Cache cache;

    private final byte[] sign;

    private final int[] index;

    private int nextBuffer;

    private float[][] buffer;

    private final float[] QD;

    SVRKernel(final SvmProblem prob, final SvmParameter param) {
        super(prob.l, prob.x, param);
        l = prob.l;
        cache = new Cache(l, (long) (param.cache_size * (1 << 20)));
        QD = new float[2 * l];
        sign = new byte[2 * l];
        index = new int[2 * l];
        for (int k = 0; k < l; k++) {
            sign[k] = 1;
            sign[k + l] = -1;
            index[k] = k;
            index[k + l] = k;
            QD[k] = (float) kernel_function(k, k);
            QD[k + l] = QD[k];
        }
        buffer = new float[2][2 * l];
        nextBuffer = 0;
    }

    void swapIndex(final int i, final int j) {
        do {
            byte other = sign[i];
            sign[i] = sign[j];
            sign[j] = other;
        } while (false);
        do {
            int other = index[i];
            index[i] = index[j];
            index[j] = other;
        } while (false);
        do {
            float other = QD[i];
            QD[i] = QD[j];
            QD[j] = other;
        } while (false);
    }

    float[] getQ(final int i, final int len) {
        float[][] data = new float[1][];
        int reali = index[i];
        if (cache.getData(reali, data, l) < l) {
            for (int j = 0; j < l; j++) {
                data[0][j] = (float) kernel_function(reali, j);
            }
        }

        // reorder and copy
        float[] buf = buffer[nextBuffer];
        nextBuffer = 1 - nextBuffer;
        byte si = sign[i];
        for (int j = 0; j < len; j++) {
            buf[j] = si * sign[j] * data[0][index[j]];
        }
        return buf;
    }

    float[] getQD() {
        return QD;
    }
}
