package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

public interface GMM extends Iterable<Gaussian> {
    double DEFAULT_FRACTION = Double.MIN_VALUE;

    float conditionalLogLh(int index, float[] x);

    float conditionalLogLh(int index, FloatVector x);

    GMM copy();

    void doEM(GMMMAPStats stats);

    void doEM(GMMMAPStats stats, boolean doWeights, boolean doMeans, boolean doVars);

    /**
     * @param r relevance factor
     */
    void doMAP(GMMMAPStats stats, final double r, boolean doWeights, boolean doMeans, boolean doVars);

    /**
     * @param r relevance factor
     */
    void doMAPonMeans(GMMMAPStats stats, final double r);

    /** Returns the feature dimension. */
    int getDimension();

    FloatVector getLogWeights();

    FloatDenseVector getMean(int index);

    /** Returns the number of mixtures. */
    int getMixtureCount();

    BayesStats getStats(float[] x, int[] indices, double fraction);

    BayesStats getStats(FloatVector x);

    BayesStats getStats(FloatVector x, double fraction);

    FloatDenseVector getVariance(int index);

    FloatVector getWeights();

    float jointLogLh(int index, float[] x);

    float jointLogLh(int index, FloatVector x);
}
