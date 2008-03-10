package net.lunglet.gmm;

import java.util.Iterator;
import net.lunglet.array4j.matrix.FloatVector;

// TODO allow precisions to be floored

// TODO allow weights to be floored

public final class DiagCovGMM implements GMM {
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

    private final float lconst;

    private final float[] logDets;

    private final float[] logWeights;

    private final float[][] means;

    private final float[][] precisions;

    public DiagCovGMM(final float[] weights, final FloatVector[] means, final FloatVector[] variances) {
        checkArguments(weights, means, variances);
        this.logWeights = new float[weights.length];
        float weightSum = 0.0f;
        for (int i = 0; i < weights.length; i++) {
            weightSum += weights[i];
        }
        this.means = new float[means.length][];
        this.precisions = new float[variances.length][];
        this.logDets = new float[means.length];
        for (int i = 0; i < logWeights.length; i++) {
            logWeights[i] = (float) Math.log(weights[i] / weightSum);
            this.means[i] = means[i].toArray();
            this.precisions[i] = variances[i].toArray();
            for (int j = 0; j < precisions[i].length; j++) {
                // convert variance to precision
                precisions[i][j] = 1.0f / precisions[i][j];
                logDets[i] += Math.log(Math.sqrt(precisions[i][j]));
            }
        }
        this.lconst = (float) (-getDimension() * 0.5 * Math.log(2 * Math.PI));
    }

    private void checkDimension(final FloatVector x) {
        if (x.length() != getDimension()) {
            throw new IllegalArgumentException();
        }
    }

    float conditionalLogLh(final int index, final float[] x) {
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

    public float conditionalLogLh(final int index, final FloatVector x) {
        checkDimension(x);
        return conditionalLogLh(index, x.toArray());
    }

    public int getDimension() {
        return means[0].length;
    }

    public int getMixtureCount() {
        return means.length;
    }

    @Override
    public Iterator<Gaussian> iterator() {
        throw new UnsupportedOperationException();
    }

    float jointLogLh(final int index, final float[] x) {
        return logWeights[index] + conditionalLogLh(index, x);
    }

    public float jointLogLh(final int index, final FloatVector x) {
        checkDimension(x);
        return jointLogLh(index, x.toArray());
    }
}
