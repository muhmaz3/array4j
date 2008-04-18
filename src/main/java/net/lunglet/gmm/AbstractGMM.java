package net.lunglet.gmm;

import java.io.Serializable;
import net.lunglet.array4j.matrix.FloatVector;

// TODO make getStats(float[], int[], double) protected here when
// it is removed from the GMM interface

public abstract class AbstractGMM implements GMM, Serializable {
    private static final long serialVersionUID = 1L;

    private void checkDimension(final FloatVector x) {
        if (x.length() != getDimension()) {
            throw new IllegalArgumentException();
        }
    }

    protected final void checkStats(final GMMMAPStats stats, final boolean doMeans, final boolean doVars) {
        if (stats.getDimension() != getDimension() || stats.getMixtureCount() != getMixtureCount()) {
            throw new IllegalArgumentException();
        }
        float[][] ex = stats.getEx();
        if (doMeans && ex == null) {
            throw new IllegalArgumentException("Statistics for estimating means not available");
        }
        float[][] exx = stats.getExx();
        if (doVars && (ex == null || exx == null)) {
            throw new IllegalArgumentException("Statistics for estimating variances not available");
        }
    }

    protected abstract double conditionalLogLh(int index, float[] x);

    public final double conditionalLogLh(final int index, final FloatVector x) {
        checkDimension(x);
        return conditionalLogLh(index, x.toArray());
    }

    public final void doEM(final GMMMAPStats stats) {
        doEM(stats, true, true, true);
    }

    public final void doMAPonMeans(final GMMMAPStats stats, final float r) {
        doMAP(stats, r, false, true, false);
    }

    public abstract BayesStats getStats(float[] x, int[] indices, double fraction);

    public final BayesStats getStats(final FloatVector x) {
        return getStats(x, MIN_FRACTION);
    }

    public final BayesStats getStats(final FloatVector x, final double fraction) {
        checkDimension(x);
        return getStats(x.toArray(), null, fraction);
    }

    protected abstract double jointLogLh(int index, float[] x);

    public final double jointLogLh(final int index, final FloatVector x) {
        checkDimension(x);
        return jointLogLh(index, x.toArray());
    }

    @Override
    public final double marginalLogLh(final FloatVector x) {
        return getStats(x).getMarginalLogLh();
    }
}
