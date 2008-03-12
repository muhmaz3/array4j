package net.lunglet.gmm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Iterator;
import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

public final class DiagCovGMM extends AbstractGMM {
    private static final long serialVersionUID = 1L;

    private static void checkArguments(final FloatVector w, final FloatVector[] u, final FloatVector[] v) {
        if (w.length() == 0) {
            throw new IllegalArgumentException();
        }
        if (w.length() != u.length) {
            throw new IllegalArgumentException();
        }
        if (w.length() != v.length) {
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

    private final double lconst;

    private transient double[] logDets;

    private transient float[] logWeights;

    private final float[][] means;

    private transient float[][] precisions;

    private final float[][] variances;

    private final float[] weights;

    /**
     * Internal copy constructor.
     */
    private DiagCovGMM(final DiagCovGMM other) {
        this.weights = other.weights.clone();
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

    public DiagCovGMM(final FloatVector weights, final FloatVector[] means, final FloatVector[] variances) {
        checkArguments(weights, means, variances);
        this.weights = weights.toArray();
        this.logWeights = new float[this.weights.length];
        this.means = new float[means.length][];
        this.variances = new float[variances.length][];
        this.precisions = new float[variances.length][];
        this.logDets = new double[variances.length];
        for (int i = 0; i < means.length; i++) {
            this.means[i] = means[i].toArray();
            this.variances[i] = variances[i].toArray();
        }
        this.lconst = -getDimension() * 0.5 * Math.log(2 * Math.PI);
        fixWeights();
        fixPrecisionsAndLogDets();
    }

    private void checkMixtureIndex(final int index) {
        if (index < 0 || index >= getMixtureCount()) {
            throw new IllegalArgumentException();
        }
    }

    public double conditionalLogLh(final int index, final float[] x) {
        double ll = 0.0f;
        float[] mean = means[index];
        float[] precision = precisions[index];
        for (int i = 0; i < mean.length; i++) {
            float d = x[i] - mean[i];
            ll += d * d * precision[i];
        }
        ll *= -0.5;
        ll += lconst;
        ll += logDets[index];
        return ll;
    }

    public DiagCovGMM copy() {
        return new DiagCovGMM(this);
    }

    @Override
    public void doEM(final GMMMAPStats stats, final boolean doWeights, final boolean doMeans, final boolean doVars) {
        checkStats(stats);
        float[] n = stats.getN();
        float[][] ex = stats.getEx();
        float[][] exx = stats.getExx();
        if ((doMeans && ex == null) || (doVars && (ex == null || exx == null))) {
            throw new IllegalArgumentException("Required stats not available");
        }
        if (doMeans || doVars) {
            ex = ex.clone();
            for (int i = 0; i < ex.length; i++) {
                float ni = n[i];
                ex[i] = ex[i].clone();
                float[] exi = ex[i];
                for (int j = 0; j < exi.length; j++) {
                    exi[j] /= ni;
                }
            }
        }
        if (doWeights) {
            System.arraycopy(n, 0, weights, 0, weights.length);
            fixWeights();
        }
        if (doMeans) {
            System.arraycopy(ex, 0, means, 0, means.length);
        }
        if (doVars) {
            for (int i = 0; i < exx.length; i++) {
                float ni = n[i];
                float[] vari = variances[i];
                float[] exi = ex[i];
                float[] exxi = exx[i];
                for (int j = 0; j < exxi.length; j++) {
                    float exij = exi[j];
                    vari[j] = exxi[j] / ni - exij * exij;
                }
            }
            fixPrecisionsAndLogDets();
        }
    }

    @Override
    public void doMAP(final GMMMAPStats stats, final double r, final boolean doWeights, final boolean doMeans,
            final boolean doVars) {
        if (doWeights || doVars) {
            throw new IllegalArgumentException();
        }
        checkStats(stats);
    }

    private void fixPrecisionsAndLogDets() {
        Arrays.fill(logDets, 0.0f);
        for (int i = 0; i < variances.length; i++) {
            float[] vari = variances[i];
            if (precisions[i] == null) {
                precisions[i] = new float[vari.length];
            }
            float[] preci = precisions[i];
            for (int j = 0; j < vari.length; j++) {
                float p = 1.0f / vari[j];
                preci[j] = p;
                logDets[i] += Math.log(Math.sqrt(p));
            }
        }
    }

    private void fixWeights() {
        double weightSum = 0.0f;
        for (int i = 0; i < weights.length; i++) {
            weightSum += weights[i];
        }
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= weightSum;
            logWeights[i] = (float) Math.log(weights[i]);
        }
    }

    public void floorVariances(final float floor) {
        for (int i = 0; i < variances.length; i++) {
            float[] vari = variances[i];
            for (int j = 0; j < vari.length; j++) {
                if (vari[j] < floor) {
                    vari[j] = floor;
                }
            }
        }
        fixPrecisionsAndLogDets();
    }

    public void floorVariances(final FloatVector floor) {
        for (int i = 0; i < variances.length; i++) {
            float[] vari = variances[i];
            float floori = floor.get(i);
            for (int j = 0; j < vari.length; j++) {
                if (vari[j] < floori) {
                    vari[j] = floori;
                }
            }
        }
        fixPrecisionsAndLogDets();
    }

    public void floorWeights(final float floorFactor) {
        float floor = floorFactor / getMixtureCount();
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < floor) {
                weights[i] = floor;
            }
        }
        fixWeights();
    }

    /** {@inheritDoc} */
    public int getDimension() {
        return means[0].length;
    }

    @Override
    public FloatVector getLogWeights() {
        return DenseFactory.valueOf(logWeights);
    }

    public FloatDenseVector getMean(final int index) {
        checkMixtureIndex(index);
        return DenseFactory.valueOf(means[index]);
    }

    /** {@inheritDoc} */
    public int getMixtureCount() {
        return means.length;
    }

    @Override
    public BayesStats getStats(final float[] x, final int[] indices, final double fraction) {
        final BayesStats stats;
        double expThresh = Math.log(fraction);
        if (indices == null) {
            stats = new FullBayesStats(getMixtureCount(), expThresh);
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
        return DenseFactory.valueOf(variances[index]);
    }

    @Override
    public FloatVector getWeights() {
        return DenseFactory.valueOf(weights);
    }

    @Override
    public Iterator<Gaussian> iterator() {
        throw new UnsupportedOperationException();
    }

    public double jointLogLh(final int index, final float[] x) {
        return logWeights[index] + conditionalLogLh(index, x);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.logWeights = new float[weights.length];
        this.precisions = new float[variances.length][];
        this.logDets = new double[variances.length];
        fixWeights();
        fixPrecisionsAndLogDets();
    }
}
