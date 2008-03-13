package net.lunglet.gmm;

public interface BayesStats {
    double MIN_EXP_THRESHOLD = Math.log(GMM.MIN_FRACTION);

    void add(int index, double aprioriLogProb, double conditionalLogLh);

    void done();

    double getMarginalLogLh();

    /**
     * @return <CODE>P(k|x)</CODE>
     */
    double[] getPosteriorProbs();
}
