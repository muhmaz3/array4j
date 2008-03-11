package net.lunglet.gmm;

import net.lunglet.array4j.math.ArraysMath;
import net.lunglet.array4j.matrix.FloatVector;

// TODO benchmark when using 1-d arrays to store ex and exx

public final class GMMMAPStats {
    private final float[][] ex;

    private final float[][] exx;

    private final GMM gmm;

    private final float[] n;

    private double totLogLh = 0.0;

    private double totN = 0.0;

    public GMMMAPStats(final GMM gmm) {
        this(gmm, true, true);
    }

    public GMMMAPStats(final GMM gmm, final boolean doMeans, final boolean doVars) {
        this.gmm = gmm;
        this.n = new float[gmm.getMixtureCount()];
        if (doMeans || doVars) {
            this.ex = new float[gmm.getMixtureCount()][];
            for (int i = 0; i < ex.length; i++) {
                ex[i] = new float[gmm.getDimension()];
            }
        } else {
            this.ex = null;
        }
        if (doVars) {
            this.exx = new float[gmm.getMixtureCount()][];
            for (int i = 0; i < exx.length; i++) {
                exx[i] = new float[gmm.getDimension()];
            }
        } else {
            this.exx = null;
        }
    }

    private void add(final float[] x) {
        BayesStats stats = gmm.getStats(x);
        totLogLh += stats.getMarginalLogLh();
        double[] posteriors = stats.getPosteriorProbs();
        final float[] xx;
        if (exx != null) {
            xx = ArraysMath.square(x);
        } else {
            xx = null;
        }
        double sum = 0.0;
        for (int m = 0; m < posteriors.length; m++) {
            double post = posteriors[m];
            if (post == 0.0) {
                continue;
            }
            n[m] += post;
            sum += post;
            for (int i = 0; i < x.length; i++) {
                if (ex != null) {
                    ex[m][i] += post * x[i];
                }
                if (exx != null) {
                    exx[m][i] += post * xx[i];
                }
            }
        }
        totN += sum;
    }

    public void add(final GMMMAPStats other) {
        if (gmm.getDimension() != other.gmm.getDimension() || gmm.getMixtureCount() != other.gmm.getMixtureCount()) {
            throw new IllegalArgumentException();
        }
    }

    public void add(final Iterable<? extends FloatVector> data) {
        for (FloatVector x : data) {
            if (x.length() != gmm.getDimension()) {
                throw new IllegalArgumentException();
            }
            add(x.toArray());
        }
    }
}
