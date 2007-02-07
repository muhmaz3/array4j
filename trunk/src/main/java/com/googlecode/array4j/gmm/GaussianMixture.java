package com.googlecode.array4j.gmm;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.Vector;

public final class GaussianMixture<E extends Gaussian> implements Gaussian {
    private final List<E> fGaussians;

    private final Vector logweights;

    public GaussianMixture(final List<E> gaussians, final double[] weights) {
        this(gaussians, DenseVector.valueOf(weights));
    }

    public GaussianMixture(final List<E> gaussians) {
        this(gaussians, createEqualWeights(gaussians.size()));
    }

    public GaussianMixture(final List<E> gaussians, final Vector weights) {
        if (gaussians.size() != weights.getDimension()) {
            throw new IllegalArgumentException();
        }
        this.fGaussians = new ArrayList<E>(gaussians);
        this.logweights = new DenseVector(weights).log();
    }

    private static Vector createEqualWeights(final int dimension) {
        final double[] weights = new double[dimension];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1.0 / dimension;
        }
        return DenseVector.valueOf(weights);
    }

    public int getDimension() {
        return logweights.getDimension();
    }

    public Vector logLikelihood(final Array data) {
        return null;
    }

    public double logLikelihood(final double... values) {
        return 0.0;
    }
}
