package net.lunglet.gmm;

import java.util.Iterator;
import net.lunglet.array4j.math.ArraysMath;
import net.lunglet.array4j.matrix.FloatVector;

// TODO benchmark when using 1-d arrays to store ex and exx

public final class GMMMAPStats {
    private final float[][] ex;

    private final float[][] exx;

    private final double fraction;

    private final transient GMM gmm;

    private final float[] n;

    private double totLogLh = 0.0;

    private double totN = 0.0;

    public GMMMAPStats(final GMM gmm) {
        this(gmm, GMM.DEFAULT_FRACTION);
    }

    public GMMMAPStats(final GMM gmm, final boolean doMeans, final boolean doVars) {
        this(gmm, GMM.DEFAULT_FRACTION, doMeans, doVars);
    }

    public GMMMAPStats(final GMM gmm, final double fraction) {
        this(gmm, fraction, true, true);
    }

    public GMMMAPStats(final GMM gmm, final double fraction, final boolean doMeans, final boolean doVars) {
        this.gmm = gmm;
        this.n = new float[gmm.getMixtureCount()];
        this.fraction = fraction;
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

    private void add(final float[] x, final int[] indices) {
        if (x.length != gmm.getDimension()) {
            throw new IllegalArgumentException();
        }
        final BayesStats stats = gmm.getStats(x, indices, fraction);
        totLogLh += stats.getMarginalLogLh();
        double[] posteriors = stats.getPosteriorProbs();
        final float[] xx;
        if (exx != null) {
            xx = ArraysMath.square(x);
        } else {
            xx = null;
        }
        double sum = 0.0;
        for (int i = 0; i < posteriors.length; i++) {
            double post = posteriors[i];
            if (post == 0.0) {
                continue;
            }
            // map from loop index to mixture index
            final int j = indices == null ? i : indices[i];
            n[j] += post;
            sum += post;
            if (ex != null) {
                float[] exj = ex[j];
                for (int k = 0; k < x.length; k++) {
                    exj[k] += post * x[k];
                }
            }
            if (exx != null) {
                float[] exxj = exx[j];
                for (int k = 0; k < x.length; k++) {
                    exxj[k] += post * xx[k];
                }
            }
        }
        totN += sum;
    }

    public void add(final FloatVector x) {
        add(x, null);
    }

    public void add(final FloatVector x, final int[] indices) {
        checkGMM();
        add(x.toArray(), indices);
    }

    public void add(final GMMMAPStats other) {
        throw new UnsupportedOperationException();
    }

    public void add(final Iterable<? extends FloatVector> data) {
        // perform check once before iterating
        checkGMM();
        for (FloatVector x : data) {
            add(x.toArray(), null);
        }
    }

    public void add(final Iterable<? extends FloatVector> data, final Iterable<int[]> indices) {
        // perform check once before iterating
        checkGMM();
        Iterator<int[]> indicesIter = indices.iterator();
        for (FloatVector x : data) {
            add(x.toArray(), indicesIter.next());
        }
    }

    private void checkGMM() {
        if (gmm == null) {
            throw new IllegalStateException("Cannot add or adapt deserialized instance");
        }
    }

    public int getDimension() {
        return ex != null ? ex[0].length : 0;
    }

    public float[][] getEx() {
        return ex;
    }

    public float[][] getExx() {
        return exx;
    }

    public int getMixtureCount() {
        return n.length;
    }

    public float[] getN() {
        return n;
    }

    public double getTotalLogLh() {
        return totLogLh;
    }

    public double getTotalN() {
        return totN;
    }
}
