package net.lunglet.gmm;

import java.util.Arrays;

public final class BayesStats {
    private final double[] apriori;

    private final double[] conditional;

    private final double eThresh;

    private final double[] joint;

    private double marginal = Double.NEGATIVE_INFINITY;

    private double max = Double.NEGATIVE_INFINITY;

    private final double[] posterior;

    public BayesStats(final int n) {
        this(n, Math.log(Double.MIN_VALUE));
    }

    public BayesStats(final int n, final double eThresh) {
        this.conditional = new double[n];
        this.apriori = new double[n];
        this.posterior = new double[n];
        this.joint = new double[n];
        this.eThresh = eThresh;
        Arrays.fill(conditional, Double.NEGATIVE_INFINITY);
        Arrays.fill(apriori, Double.NEGATIVE_INFINITY);
        Arrays.fill(joint, Double.NEGATIVE_INFINITY);
    }

    void add(final int index, final double aprioriLogProb, final double conditionalLogLh) {
        apriori[index] = aprioriLogProb;
        conditional[index] = conditionalLogLh;
        double x = aprioriLogProb + conditionalLogLh;
        joint[index] = x;
        if (x > max) {
            max = x;
        }
    }

    void done() {
        double sum = 1.0;
        for (int j = 0; j < joint.length; j++) {
            sum += exp(joint[j] - max);
        }
        marginal = Math.log(sum) + max;
        for (int j = 0; j < posterior.length; j++) {
            posterior[j] = exp(joint[j] - marginal);
        }
    }

    private double exp(final double x) {
        if (x < eThresh) {
            return 0.0;
        } else {
            return Math.exp(x);
        }
    }

    /**
     * @return <CODE>log P(k)</CODE>
     */
    public double getAprioriLogProb(final int index) {
        return apriori[index];
    }

    /**
     * @return <CODE>log p(x|k)</CODE>
     */
    public double getConditionalLogLh(final int index) {
        return conditional[index];
    }

    /**
     * @return <CODE>p(k,x)</CODE>
     */
    public double getJointLogLh(final int index) {
        return joint[index];
    }

    public double getMarginalLogLh() {
        return marginal;
    }

    /**
     * @return <CODE>P(k|x)</CODE>
     */
    public double getPosteriorProb(final int index) {
        return posterior[index];
    }

    /**
     * @return <CODE>P(k|x)</CODE>
     */
    public double[] getPosteriorProbs() {
        return posterior;
    }
}
