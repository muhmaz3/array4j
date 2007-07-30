package net.lunglet.svm;

import com.googlecode.array4j.FloatVector;

abstract class Kernel extends QMatrix {
    private FloatVector<?>[] x;

    private final double[] x_square;

    // svm_parameter
    private final int kernel_type;

    private final int degree;

    private final double gamma;

    private final double coef0;

    abstract float[] getQ(int column, int len);

    abstract float[] getQD();

    void swapIndex(final int i, final int j) {
        do {
            FloatVector<?> other = x[i];
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

    private static double powi(final double base, final int times) {
        double tmp = base, ret = 1.0;

        for (int t = times; t > 0; t /= 2) {
            if (t % 2 == 1) {
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
            throw new UnsupportedOperationException();
        default:
            throw new AssertionError();
        }
    }

    Kernel(final int l, final FloatVector<?>[] x_, final SvmParameter param) {
        this.kernel_type = param.kernel_type;
        this.degree = param.degree;
        this.gamma = param.gamma;
        this.coef0 = param.coef0;

        x = (FloatVector<?>[]) x_.clone();

        if (kernel_type == SvmParameter.RBF) {
            x_square = new double[l];
            for (int i = 0; i < l; i++) {
                x_square[i] = dot(x[i], x[i]);
            }
        } else {
            x_square = null;
        }
    }

    static double dot(final FloatVector<?> x, final FloatVector<?> y) {
        throw new UnsupportedOperationException();
    }

    static double k_function(final FloatVector<?> x, final FloatVector<?> y, final SvmParameter param) {
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
            throw new UnsupportedOperationException();
        default:
            throw new AssertionError();
        }
    }
}
