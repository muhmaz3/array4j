package net.lunglet.gmm;

// TODO use indices to map requests for log weights, etc.

public final class FastBayesStats extends AbstractBayesStats {
    private final int[] indices;

    public FastBayesStats(final int[] indices, final double expThresh) {
        super(indices.length, expThresh);
        this.indices = indices;
    }
}
