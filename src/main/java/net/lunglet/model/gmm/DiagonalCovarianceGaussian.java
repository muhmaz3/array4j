package net.lunglet.model.gmm;

import net.lunglet.array4j.math.FloatMatrixMath;
import net.lunglet.array4j.matrix.FloatVector;

public final class DiagonalCovarianceGaussian extends AbstractGaussian implements Gaussian {
    private final double lloffset;

    private final FloatVector mean;

    private final FloatVector var;

    public DiagonalCovarianceGaussian(final FloatVector mean, final FloatVector var) {
        if (mean.length() != var.length()) {
            throw new IllegalArgumentException();
        }
        this.mean = mean;
        // TODO store precision instead of variance
        this.var = var;
        int dim = getDimension();
        double lloffset = 0.0;
        for (int i = 0; i < dim; i++) {
            lloffset += Math.log(var.get(i));
        }
        lloffset *= -0.5;
        lloffset -= 0.5 * dim * Math.log(2 * Math.PI);
        this.lloffset = lloffset;
    }

    @Override
    public int getDimension() {
        return mean.length();
    }

    @Override
    public double logLikelihood(final FloatVector x) {
        checkInput(x);
        if (false) {
            int dim = getDimension();
            double ll = 0.0;
            for (int i = 0; i < dim; i++) {
                double y = x.get(i) - mean.get(i);
                ll += y * y / var.get(i);
            }
            ll *= -0.5;
            ll += lloffset;
            return ll;
        } else {
            FloatVector y = FloatMatrixMath.minus(x, mean);
            return 0.0;
        }
    }
}
