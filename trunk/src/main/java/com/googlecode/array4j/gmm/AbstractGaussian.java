package com.googlecode.array4j.gmm;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.Vector;

public abstract class AbstractGaussian implements Gaussian {
    private final int fDimension;

    public AbstractGaussian(final int dimension) {
        this.fDimension = dimension;
    }

    public abstract Vector logLikelihood(final Array data);

    public final double logLikelihood(final double... values) {
        final DenseVector x = DenseVector.valueOf(values);
        if (x.length() != getDimension()) {
            throw new IllegalArgumentException();
        }
        return logLikelihood(x).get(0);
    }

    public final int getDimension() {
        return fDimension;
    }
}
