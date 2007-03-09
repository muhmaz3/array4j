package com.googlecode.array4j.cluster;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.DenseVector;
import com.googlecode.array4j.RowVector;
import com.googlecode.array4j.matrix.Matrix;
import com.googlecode.array4j.matrix.Vector;

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
            for (int j = 0; j < data.rows(); j++) {
                double distortion = example(data.row(i));
            }
        }
        throw new UnsupportedOperationException();
    }

    private double example(final RowVector x) {
        // TODO this could be faster in the native case if we make fewer
        // function calls
        for (int i = 0; i < fCodebook.rows(); i++) {
//            fCodebook.row(i);
        }
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
