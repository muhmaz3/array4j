package net.lunglet.svm;

final class OneClassKernel extends Kernel {
    private final Cache cache;

    private final float[] QD;

    OneClassKernel(final SvmProblem prob, final SvmParameter param) {
        super(prob.l, prob.x, prob.gram, param);
        cache = new Cache(prob.l, (long) (param.cache_size * (1 << 20)));
        QD = new float[prob.l];
        for (int i = 0; i < prob.l; i++) {
            QD[i] = (float) kernel_function(i, i);
        }
    }

    @Override
    float[] getQ(final int i, final int len) {
        float[][] data = new float[1][];
        int start;
        if ((start = cache.getData(i, data, len)) < len) {
            for (int j = start; j < len; j++) {
                data[0][j] = (float) kernel_function(i, j);
            }
        }
        return data[0];
    }

    @Override
    float[] getQD() {
        return QD;
    }

    @Override
    void swapIndex(final int i, final int j) {
        cache.swapIndex(i, j);
        super.swapIndex(i, j);
        do {
            float _ = QD[i];
            QD[i] = QD[j];
            QD[j] = _;
        } while (false);
    }
}
