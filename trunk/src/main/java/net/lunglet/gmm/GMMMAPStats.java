package net.lunglet.gmm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.util.ArrayMath;
import net.lunglet.util.AssertUtils;

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
        this(gmm, GMM.MIN_FRACTION);
    }

    public GMMMAPStats(final GMM gmm, final boolean doMeans, final boolean doVars) {
        this(gmm, GMM.MIN_FRACTION, doMeans, doVars);
    }

    public GMMMAPStats(final GMM gmm, final double fraction) {
        this(gmm, fraction, true, true);
    }

    public GMMMAPStats(final GMM gmm, final double fraction, final boolean doMeans, final boolean doVars) {
        // TODO check upper bound on fraction
        if (fraction < 0) {
            throw new IllegalArgumentException();
        }
        this.gmm = gmm;
        this.n = new float[gmm.getMixtureCount()];
        this.fraction = fraction < GMM.MIN_FRACTION ? GMM.MIN_FRACTION : fraction;
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

    private void add(final float[] x, final BayesStats stats, final double[] posteriors, final int[] indices) {
        AssertUtils.assertTrue(indices == null || posteriors.length == indices.length);
        final float[] xx;
        if (exx != null) {
            xx = ArrayMath.square(x);
        } else {
            xx = null;
        }
        double sum = 0.0;
        for (int i = 0; i < posteriors.length; i++) {
            double post = posteriors[i];
            if (post == 0.0) {
                continue;
            }
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

    private int[] add(final float[] x, final int c) {
        if (x.length != gmm.getDimension()) {
            throw new IllegalArgumentException();
        }
        final BayesStats stats = gmm.getStats(x, null, fraction);
        double[] posteriors = stats.getPosteriorProbs();
        int[] indices = ArrayMath.argmaxn(posteriors, c);
        double[] postpart = new double[indices.length];
        for (int i = 0; i < indices.length; i++) {
            postpart[i] = posteriors[indices[i]];
        }
        add(x, stats, postpart, indices);
        totLogLh += stats.getMarginalLogLh(indices);
        return indices;
    }

    private void add(final float[] x, final int[] indices) {
        if (x.length != gmm.getDimension()) {
            throw new IllegalArgumentException();
        }
        final BayesStats stats = gmm.getStats(x, indices, fraction);
        add(x, stats, stats.getPosteriorProbs(), indices);
        totLogLh += stats.getMarginalLogLh();
    }

    public void add(final FloatVector x) {
        add(x, null);
    }

    public void add(final FloatVector x, final int[] indices) {
        checkGMM();
        add(x.toArray(), indices);
    }

    public void add(final GMMMAPStats other) {
        for (int i = 0; i < n.length; i++) {
            n[i] += other.n[i];
            float[] exi = ex[i];
            float[] otherexi = other.ex[i];
            float[] exxi = exx[i];
            float[] otherexxi = other.exx[i];
            for (int j = 0; j < exi.length; j++) {
                exi[j] += otherexi[j];
                exxi[j] += otherexxi[j];
            }
        }
        totLogLh += other.totLogLh;
        totN += other.totN;
    }

    public void add(final Iterable<? extends FloatVector> data) {
        checkGMM();
        for (FloatVector x : data) {
            add(x.toArray(), null);
        }
    }

    public List<int[]> add(final Iterable<? extends FloatVector> data, final int c) {
        checkGMM();
        List<int[]> indicesList = new ArrayList<int[]>();
        for (FloatVector x : data) {
            int[] indices = add(x.toArray(), c);
            indicesList.add(indices);
        }
        return indicesList;
    }

    public void add(final Iterable<? extends FloatVector> data, final Iterable<int[]> indices) {
        // perform check once before iterating
        checkGMM();
        Iterator<int[]> indicesIter = indices.iterator();
        for (FloatVector x : data) {
            int[] xindices = indicesIter.next();
            AssertUtils.assertNotNull(xindices);
            add(x.toArray(), xindices);
        }
        AssertUtils.assertTrue(!indicesIter.hasNext());
    }

    private void checkGMM() {
        if (gmm == null) {
            throw new IllegalStateException("Cannot add to or adapt without GMM");
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
