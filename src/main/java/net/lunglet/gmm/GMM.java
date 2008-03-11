package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

public interface GMM extends Iterable<Gaussian> {
    double DEFAULT_FRACTION = Double.MIN_VALUE;

    float conditionalLogLh(int index, float[] x);

    float conditionalLogLh(int index, FloatVector x);

    GMM copy();

    /** Returns the feature dimension. */
    int getDimension();

    FloatDenseVector getMean(int index);

    FloatDenseVector getVariance(int index);

    /** Returns the number of mixtures. */
    int getMixtureCount();

    BayesStats getStats(float[] x, int[] indices, double fraction);

    BayesStats getStats(FloatVector x);

    BayesStats getStats(FloatVector x, double fraction);

    float jointLogLh(int index, float[] x);

    float jointLogLh(int index, FloatVector x);
}
