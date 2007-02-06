package com.googlecode.array4j.gmm;

import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Vector;

public final class DiagonalGaussian extends Gaussian {
    private final Vector fMean;

    private final Vector fVariance;

    private final double sumlogvariances;

    private final double ndimlog2pi;

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
        this.sumlogvariances = variance.log().sum();
        this.ndimlog2pi = getDimension() * Math.log(2.0 * Math.PI);
    }

    @Override
    public Vector logLikelihood(final Matrix x) {
        if (x.getColumns() != getDimension()) {
            throw new IllegalArgumentException();
        }
        final Vector logLikelihoods = DenseVector.valueOf(new double[x.getRows()]);
        throw new UnsupportedOperationException();
//        logLikelihood(getDimension(), x.getRows(), x.buffer, fMean.buffer, fVariance.buffer, logLikelihoods.buffer);
//        return logLikelihoods;
    }

    @Override
    public double logLikelihood(final double... values) {
        final DenseVector valuesVec = DenseVector.valueOf(values);
        throw new UnsupportedOperationException();
    }
}
