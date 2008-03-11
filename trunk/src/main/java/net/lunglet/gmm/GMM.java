package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;

public interface GMM extends Iterable<Gaussian> {
    float conditionalLogLh(int index, float[] x);

    float conditionalLogLh(int index, FloatVector x);

    /** Returns the feature dimension. */
    int getDimension();

    /** Returns the number of mixtures. */
    int getMixtureCount();

    float jointLogLh(int index, float[] x);

    float jointLogLh(int index, FloatVector x);

    BayesStats getStats(float[] x);

    BayesStats getStats(float[] x, double fraction);
}
