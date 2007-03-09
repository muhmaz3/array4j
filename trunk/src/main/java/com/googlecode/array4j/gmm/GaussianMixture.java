package com.googlecode.array4j.gmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.matrix.Vector;

public class GaussianMixture extends AbstractGaussian {
    private final List<Gaussian> fGaussians;

    private final Vector logweights;

    public GaussianMixture(final List<Gaussian> gaussians, final double[] weights) {
        this(gaussians, DenseVector.valueOf(weights));
    }

    public GaussianMixture(final Gaussian... gaussians) {
        this(Arrays.asList(gaussians));
    }

    public GaussianMixture(final List<Gaussian> gaussians) {
        this(gaussians, createEqualWeights(gaussians.size()));
    }

    public GaussianMixture(final List<Gaussian> gaussians, final Vector weights) {
        super(checkDimension(gaussians));
        if (gaussians.size() != weights.getDimension()) {
            throw new IllegalArgumentException();
        }
        this.fGaussians = new ArrayList<Gaussian>(gaussians);
        this.logweights = new DenseVector(weights).log();
    }

    private static int checkDimension(final List<Gaussian> gaussians) {
        if (gaussians.size() == 0) {
            throw new IllegalArgumentException("must have at least one component");
        }
        final int dim = gaussians.get(0).getDimension();
        for (final Gaussian component : gaussians) {
            if (dim != component.getDimension()) {
                throw new IllegalArgumentException("all components must have the same dimension");
            }
        }
        return dim;
    }

    private static Vector createEqualWeights(final int dimension) {
        final double[] weights = new double[dimension];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1.0 / dimension;
        }
        return DenseVector.valueOf(weights);
    }

    public final Vector logLikelihood(final Array data) {
        final Vector lls = new DenseVector(data.shape(0));
        int i = 0;
        for (final Gaussian component : fGaussians) {
            lls.plusEquals(component.logLikelihood(data));
            lls.plusEquals(logweights.get(i++));
        }
        return lls;
    }
}
