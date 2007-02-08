package com.googlecode.array4j.cluster;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.Matrix;
import com.googlecode.array4j.Vector;

public class KMeans {
    private final Matrix fCodebook;

    private final Vector clusterCounts;

    public KMeans(final Matrix codebook) {
        // TODO should clone original codebook here?
        this.fCodebook = codebook;
        this.clusterCounts = new DenseVector(codebook.shape(0));
    }

    public final void train(final Matrix data, final int niter) {
        train(data, niter, 1.0e-4);
    }

    public final void train(final Matrix data, final int niter, final double threshold) {
        for (int i = 0; i < niter; i++) {
            for (int j = 0; j < data.shape(1); j++) {
                double distortion = example(data.getRow(i));
            }
        }
        throw new UnsupportedOperationException();
    }

    private double example(final Vector x) {
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
