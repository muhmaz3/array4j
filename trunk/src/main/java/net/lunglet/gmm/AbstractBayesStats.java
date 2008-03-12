package net.lunglet.gmm;

import java.util.Arrays;

// TODO maybe make reset public so that stats objects can be reused

public abstract class AbstractBayesStats implements BayesStats {
    protected final double[] apriori;

    protected final double[] conditional;

    private boolean done = false;

    /** Threshold for argument of exponential function. */
    private final double expThresh;

    protected final double[] joint;

    private double marginal;

    private double max;

    private final double[] posterior;

    public AbstractBayesStats(final int n) {
        this(n, DEFAULT_EXP_THRESH);
    }

    public AbstractBayesStats(final int n, final double expThresh) {
        this.conditional = new double[n];
        this.apriori = new double[n];
        this.posterior = new double[n];
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

    public final void done() {
        if (done) {
            throw new IllegalStateException();
        }
        double sum = 0.0;
        for (int i = 0; i < joint.length; i++) {
            sum += exp(joint[i] - max);
        }
        marginal = Math.log(sum) + max;
        for (int i = 0; i < posterior.length; i++) {
            posterior[i] = exp(joint[i] - marginal);
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

    public final double[] getPosteriorProbs() {
        checkState();
        return posterior;
    }

    private void reset() {
        marginal = Double.NEGATIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
        Arrays.fill(conditional, Double.NEGATIVE_INFINITY);
        Arrays.fill(apriori, Double.NEGATIVE_INFINITY);
        Arrays.fill(joint, Double.NEGATIVE_INFINITY);
        Arrays.fill(posterior, 0.0);
    }
}
