package net.lunglet.gmm;

public interface BayesStats {
    double DEFAULT_EXP_THRESH = Math.log(GMM.DEFAULT_FRACTION);

    void add(int index, double aprioriLogProb, double conditionalLogLh);

    void done();

    double getMarginalLogLh();

    /**
     * @return <CODE>P(k|x)</CODE>
     */
    double[] getPosteriorProbs();
}
