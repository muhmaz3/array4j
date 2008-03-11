package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;

// TODO utility function to split GMM

public final class GMMUtils {
    public DiagCovGMM trainDiagCovGMM(final int dimension, final int mixtures,
            final Iterable<? extends FloatVector> data, final int iterations) {
        // start with one component gmm
        DiagCovGMM gmm = null;

        GMMMAPStats stats = new GMMMAPStats(gmm);
        // TODO start a thread pool
        // TODO send various parts of data to threads
        // collect stats objects
        stats.add(data);

//        stats.emAdapt();

        // TODO maybe floor variances of GMM using a constant
        // TODO floor variances after the first iteration
//        gmm.floorVariances();


        return null;
    }

    private GMMUtils() {
    }
}
