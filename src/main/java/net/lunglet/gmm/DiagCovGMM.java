package net.lunglet.gmm;

import java.util.Arrays;
import java.util.Iterator;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

public final class DiagCovGMM extends AbstractGMM {
    private static final long serialVersionUID = 1L;

    private static void checkArguments(final float[] w, final FloatVector[] u, final FloatVector[] v) {
        if (w.length == 0) {
            throw new IllegalArgumentException();
        }
        if (w.length != u.length) {
            throw new IllegalArgumentException();
        }
        if (w.length != v.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < u.length; i++) {
            if (u[i].length() != u[0].length()) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < v.length; i++) {
            if (v[i].length() != u[0].length()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static float[] getNormalizedLogWeights(final float[] weights) {
        float weightSum = 0.0f;
        for (int i = 0; i < weights.length; i++) {
            weightSum += weights[i];
        }
        float[] logWeights = new float[weights.length];
        for (int i = 0; i < weights.length; i++) {
            logWeights[i] = (float) Math.log(weights[i] / weightSum);
        }
        return logWeights;
    }

    private final float lconst;

    private final float[] logDets;

    private float[] logWeights;

    private final float[][] means;

    private final float[][] variances;

    private final float[][] precisions;

    /**
     * Internal copy constructor.
     */
    private DiagCovGMM(final DiagCovGMM other) {
        this.logWeights = other.logWeights.clone();
        this.means = other.means.clone();
        this.variances = other.variances.clone();
        this.precisions = other.precisions.clone();
        this.logDets = other.logDets.clone();
        this.lconst = other.lconst;
        // make a deep copy
        for (int i = 0; i < means.length; i++) {
            means[i] = means[i].clone();
            variances[i] = variances[i].clone();
            precisions[i] = precisions[i].clone();
        }
    }

    public DiagCovGMM(final float[] weights, final FloatVector[] means, final FloatVector[] variances) {
        checkArguments(weights, means, variances);
        this.logWeights = getNormalizedLogWeights(weights);
        this.means = new float[means.length][];
        this.variances = new float[variances.length][];
        this.precisions = new float[variances.length][];
        this.logDets = new float[variances.length];
        for (int i = 0; i < means.length; i++) {
            this.means[i] = means[i].toArray();
            this.variances[i] = variances[i].toArray();
        }
        this.lconst = (float) (-getDimension() * 0.5 * Math.log(2 * Math.PI));
        setPrecisionsAndLogDets();
    }

    private void setPrecisionsAndLogDets() {
        Arrays.fill(logDets, 0.0f);
        for (int i = 0; i < variances.length; i++) {
            float[] vari = variances[i];
            for (int j = 0; j < vari.length; j++) {
                float p = 1.0f / vari[j];
                precisions[i][j] = p;
                logDets[i] += Math.log(Math.sqrt(p));
            }
        }
    }

    private void checkMixtureIndex(final int index) {
        if (index < 0 || index >= getMixtureCount()) {
            throw new IllegalArgumentException();
        }
    }

    public float conditionalLogLh(final int index, final float[] x) {
        float ll = 0.0f;
        float[] mean = means[index];
        float[] precision = precisions[index];
        for (int i = 0; i < mean.length; i++) {
            ll += (x[i] - mean[i]) * precision[i];
        }
        ll *= -0.5f;
        ll += lconst;
        ll += logDets[index];
        return ll;
    }

    public DiagCovGMM copy() {
        return new DiagCovGMM(this);
    }

    /** {@inheritDoc} */
    public int getDimension() {
        return means[0].length;
    }

    public FloatDenseVector getMean(final int index) {
        checkMixtureIndex(index);
        return DenseFactory.valueOf(means[index]);
    }

    /** {@inheritDoc} */
    public int getMixtureCount() {
        return means.length;
    }

    public void getStats(final BayesStats stats, final float[] x, final int[] indices) {
        // TODO check that stats object can handle specified number of indices
        // by looking at stats.getSize() vs getDimension() or indices.length
        // TODO reset stats object
        // TODO do the same stuff as the other stats method
        throw new UnsupportedOperationException();
    }

    @Override
    public BayesStats getStats(final float[] x, final int[] indices, final double fraction) {
        final BayesStats stats;
        double expThresh = Math.log(fraction);
        if (indices == null) {
            stats = new FullBayesStats(getDimension(), expThresh);
            int mixtures = getMixtureCount();
            for (int i = 0; i < mixtures; i++) {
                stats.add(i, logWeights[i], conditionalLogLh(i, x));
            }
        } else {
            stats = new FastBayesStats(indices, expThresh);
            for (int i = 0; i < indices.length; i++) {
                int j = indices[i];
                stats.add(i, logWeights[j], conditionalLogLh(j, x));
            }
        }
        stats.done();
        return stats;
    }

    @Override
    public FloatDenseVector getVariance(final int index) {
        checkMixtureIndex(index);
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Gaussian> iterator() {
        throw new UnsupportedOperationException();
    }

    public float jointLogLh(final int index, final float[] x) {
        return logWeights[index] + conditionalLogLh(index, x);
    }
}
