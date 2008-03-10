package net.lunglet.gmm;

import net.lunglet.array4j.matrix.FloatVector;

public interface GMM extends Iterable<Gaussian> {
    float conditionalLogLh(final int index, final FloatVector x);

    float jointLogLh(final int index, final FloatVector x);
}
