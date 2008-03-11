package net.lunglet.gmm;

import java.io.Serializable;
import net.lunglet.array4j.matrix.FloatVector;

public abstract class AbstractGMM implements GMM, Serializable {
    private static final long serialVersionUID = 1L;

    private void checkDimension(final FloatVector x) {
        if (x.length() != getDimension()) {
            throw new IllegalArgumentException();
        }
    }

    protected final void checkStats(final GMMMAPStats stats) {
        if (stats.getDimension() != getDimension() || stats.getMixtureCount()  != getMixtureCount()) {
            throw new IllegalArgumentException();
        }
    }

    public final float conditionalLogLh(final int index, final FloatVector x) {
        checkDimension(x);
        return conditionalLogLh(index, x.toArray());
    }

    public final BayesStats getStats(final FloatVector x) {
        return getStats(x, DEFAULT_FRACTION);
    }

    public final BayesStats getStats(final FloatVector x, final double fraction) {
        checkDimension(x);
        return getStats(x.toArray(), null, fraction);
    }

    public final float jointLogLh(final int index, final FloatVector x) {
        checkDimension(x);
        return jointLogLh(index, x.toArray());
    }
}
