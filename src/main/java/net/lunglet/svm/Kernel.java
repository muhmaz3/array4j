package net.lunglet.svm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.math.FloatMatrixMath;

abstract class Kernel extends QMatrix {
    static double dot(final FloatVector x, final SvmNode y) {
        return FloatMatrixMath.dot(x, y.getValue());
    }

    static double dot(final SvmNode x, final SvmNode y) {
        return FloatMatrixMath.dot(x.getValue(), y.getValue());
    }

    static double k_function(final FloatVector x, final SvmNode y, final SvmParameter param) {
        switch (param.kernel_type) {
        case SvmParameter.LINEAR:
            return dot(x, y);
        case SvmParameter.POLY:
            return powi(param.gamma * dot(x, y) + param.coef0, param.degree);
        case SvmParameter.RBF:
            throw new UnsupportedOperationException();
        case SvmParameter.SIGMOID:
            return tanh(param.gamma * dot(x, y) + param.coef0);
        case SvmParameter.PRECOMPUTED:
            // This should never happen. If it does, it's probably because the
            // svm type parameter wasn't fixed after training.
            throw new AssertionError();
        default:
            throw new AssertionError();
        }
    }

    private static double powi(final double base, final int times) {
        double tmp = base, ret = 1.0;

        for (int t = times; t > 0; t /= 2) {
            if ((t & 1) == 1) {
                ret *= tmp;
            }
            tmp = tmp * tmp;
        }
        return ret;
    }

    private static double tanh(final double x) {
        double e = Math.exp(x);
        return 1.0 - 2.0 / (e * e + 1);
    }

    private final double coef0;

    private final int degree;

    private final double gamma;

    private final PrecomputedKernel kernel;

    private final int kernel_type;

    private final SvmNode[] x;

    private final double[] x_square;

    Kernel(final int l, final SvmNode[] x, final PrecomputedKernel kernel, final SvmParameter param) {
        this.x = x.clone();
        this.kernel = kernel;
        this.kernel_type = param.kernel_type;
        this.degree = param.degree;
        this.gamma = param.gamma;
        this.coef0 = param.coef0;
        if (kernel_type == SvmParameter.RBF) {
            x_square = new double[l];
            for (int i = 0; i < l; i++) {
                x_square[i] = dot(x[i], x[i]);
            }
        } else {
            x_square = null;
        }
    }

    @Override
    abstract float[] getQ(int column, int len);

    @Override
    abstract float[] getQD();

    double kernel_function(final int i, final int j) {
        switch (kernel_type) {
        case SvmParameter.LINEAR:
            return dot(x[i], x[j]);
        case SvmParameter.POLY:
            return powi(gamma * dot(x[i], x[j]) + coef0, degree);
        case SvmParameter.RBF:
            return Math.exp(-gamma * (x_square[i] + x_square[j] - 2 * dot(x[i], x[j])));
        case SvmParameter.SIGMOID:
            return tanh(gamma * dot(x[i], x[j]) + coef0);
        case SvmParameter.PRECOMPUTED:
            return kernel.get(x[i].getIndex(), x[j].getIndex());
        default:
            throw new AssertionError();
        }
    }

    @Override
    void swapIndex(final int i, final int j) {
        do {
            SvmNode other = x[i];
            x[i] = x[j];
            x[j] = other;
        } while (false);
        if (x_square != null) {
            do {
                double other = x_square[i];
                x_square[i] = x_square[j];
                x_square[j] = other;
            } while (false);
        }
    }
}
