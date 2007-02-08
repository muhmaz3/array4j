package com.googlecode.array4j.cluster;

import static com.googlecode.array4j.ArrayUtils.checkExactly2d;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.Vector;

public class KMeans {
    private final Array fCodebook;

    private final Vector clusterCounts;

    public KMeans(final Array codebook) {
        checkExactly2d(codebook);
        // TODO should clone original codebook here?
        this.fCodebook = codebook;
        this.clusterCounts = new DenseVector(codebook.shape(0));
    }

    public final void train(final Array data, final int niter) {
        train(data, niter, 1.0e-4);
    }

    public final void train(final Array data, final int niter, final double threshold) {
        throw new UnsupportedOperationException();
    }

    private void example(final Array data) {
        throw new UnsupportedOperationException();
    }

    private void reset() {
        clusterCounts.fill(0.0);
    }

    public final Array getCodebook() {
        // TODO should clone before returning internal structure?
        return fCodebook;
    }
}
