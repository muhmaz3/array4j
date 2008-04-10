package net.lunglet.gmm;

import java.util.Arrays;
import net.lunglet.util.ArrayMath;

public abstract class AbstractBayesStats implements BayesStats {
    protected final double[] apriori;

    protected final double[] conditional;

    private boolean done = false;

    /** Threshold for argument of exponential function. */
    private final double expThresh;

    protected final double[] joint;

    private double marginal;

    private double max;

    private final double[] posteriors;

    public AbstractBayesStats(final int n) {
        this(n, MIN_EXP_THRESHOLD);
    }

    public AbstractBayesStats(final int n, final double expThresh) {
        this.conditional = new double[n];
        this.apriori = new double[n];
        this.posteriors = new double[n];
        this.joint = new double[n];
        this.expThresh = expThresh;
        this.marginal = Double.NEGATIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
        Arrays.fill(conditional, Double.NEGATIVE_INFINITY);
        Arrays.fill(apriori, Double.NEGATIVE_INFINITY);
        Arrays.fill(joint, Double.NEGATIVE_INFINITY);
    }

    public final void add(final int i, final double aprioriLogProb, final double conditionalLogLh) {
        apriori[i] = aprioriLogProb;
        conditional[i] = conditionalLogLh;
        double x = aprioriLogProb + conditionalLogLh;
        joint[i] = x;
        if (x > max) {
            max = x;
        }
    }

    private void checkState() {
        if (!done) {
            throw new IllegalStateException();
        }
    }

    private double logsumexp(final double[] values, final double max) {
        double sum = 0.0;
        for (int i = 0; i < values.length; i++) {
            sum += exp(values[i] - max);
        }
        return max + Math.log(sum);
    }

    public final void done() {
        if (done) {
            throw new IllegalStateException();
        }
        marginal = logsumexp(joint, max);
        for (int i = 0; i < posteriors.length; i++) {
            posteriors[i] = exp(joint[i] - marginal);
        }
        done = true;
        checkState();
    }

    private double exp(final double x) {
        if (x < expThresh) {
            return 0.0;
        } else {
            return Math.exp(x);
        }
    }

    public final double getMarginalLogLh() {
        checkState();
        return marginal;
    }

    public final double getMarginalLogLh(final int[] indices) {
        if (indices == null) {
            return marginal;
        }
        double[] jointpart = new double[indices.length];
        for (int i = 0; i < indices.length; i++) {
            jointpart[i] = joint[indices[i]];
        }
        return logsumexp(jointpart, ArrayMath.max(jointpart));
    }

    public final double[] getPosteriorProbs() {
        checkState();
        return posteriors;
    }
}
