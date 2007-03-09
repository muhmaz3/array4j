package com.googlecode.array4j.gmm;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.kernel.Interface;
import com.googlecode.array4j.matrix.Vector;

public class DiagonalGaussian extends AbstractGaussian {
    private final Vector fMean;

    private final Vector fVariance;

    private final double llconst;

    public DiagonalGaussian(final double[] mean, final double[] variance) {
        this(DenseVector.valueOf(mean), DenseVector.valueOf(variance));
    }

    public DiagonalGaussian(final Vector mean, final Vector variance) {
        super(mean.getDimension());
        if (mean.length() != variance.length()) {
            throw new IllegalArgumentException();
        }
        this.fMean = mean;
        this.fVariance = variance;
        this.llconst = variance.log().sum() + getDimension() * Math.log(2.0 * Math.PI);
    }

    @Override
    public final Vector logLikelihood(final Array data) {
        if (data.ndim() != 2 || data.shape(1) != getDimension()) {
            throw new IllegalArgumentException();
        }
        final Vector logLikelihoods = new DenseVector(data.shape(0));
        Interface.kernel().diagonalLogLikelihood(data.getShape(), fMean.getBuffer(), fVariance.getBuffer(),
                data.getBuffer(), logLikelihoods.getBuffer());
        logLikelihoods.plusEquals(llconst);
        logLikelihoods.timesEquals(-0.5);
        return logLikelihoods;
    }
}
