package net.lunglet.svm;

final class SVCKernel extends Kernel {
    private final byte[] y;

    private final Cache cache;

    private final float[] QD;

    SVCKernel(final SvmProblem prob, final SvmParameter param, final byte[] y_) {
        super(prob.l, prob.x, param);
        y = (byte[]) y_.clone();
        cache = new Cache(prob.l, (long) (param.cache_size * (1 << 20)));
        QD = new float[prob.l];
        for (int i = 0; i < prob.l; i++) {
            QD[i] = (float) kernel_function(i, i);
        }
    }

    float[] getQ(final int i, final int len) {
        float[][] data = new float[1][];
        int start;
        if ((start = cache.getData(i, data, len)) < len) {
            for (int j = start; j < len; j++) {
                data[0][j] = (float) (y[i] * y[j] * kernel_function(i, j));
            }
        }
        return data[0];
    }

    float[] getQD() {
        return QD;
    }

    void swapIndex(final int i, final int j) {
        cache.swapIndex(i, j);
        super.swapIndex(i, j);
        do {
            byte other = y[i];
            y[i] = y[j];
            y[j] = other;
        } while (false);
        do {
            float other = QD[i];
            QD[i] = QD[j];
            QD[j] = other;
        } while (false);
    }
}
