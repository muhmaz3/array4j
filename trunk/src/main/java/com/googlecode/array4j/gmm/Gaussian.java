package com.googlecode.array4j.gmm;

import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Vector;

public abstract class Gaussian {
    private final int fDimension;

    public Gaussian(final int dimension) {
        this.fDimension = dimension;
    }

    public abstract Vector logLikelihood(final Matrix x);

    public abstract double logLikelihood(final double... values);

    public final int getDimension() {
        return fDimension;
    }
}
