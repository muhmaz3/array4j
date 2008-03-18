package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

// TODO remove getStats(float[], int[], double) from the interface

public interface GMM extends Iterable<Gaussian> {
    double MIN_FRACTION = Double.MIN_VALUE;

    double conditionalLogLh(int index, FloatVector x);

    GMM copy();

    void doEM(GMMMAPStats stats);

    void doEM(GMMMAPStats stats, boolean doWeights, boolean doMeans, boolean doVars);

    /**
     * @param r relevance factor
     */
    void doMAP(GMMMAPStats stats, final float r, boolean doWeights, boolean doMeans, boolean doVars);

    /**
     * @param r relevance factor
     */
    void doMAPonMeans(GMMMAPStats stats, final float r);

    /** Returns the feature dimension. */
    int getDimension();

    FloatVector getLogWeights();

    FloatDenseVector getMean(int index);

    /** Returns the number of mixtures. */
    int getMixtureCount();

    BayesStats getStats(float[] x, int[] indices, double fraction);

    BayesStats getStats(FloatVector x);

    BayesStats getStats(FloatVector x, double fraction);

    FloatVector getWeights();

    double jointLogLh(int index, FloatVector x);

    double marginalLogLh(FloatVector x);
}
