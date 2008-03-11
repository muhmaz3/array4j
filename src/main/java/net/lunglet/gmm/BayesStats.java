package net.lunglet.gmm;

public interface BayesStats {
    void add(int index, double aprioriLogProb, double conditionalLogLh);

    void done();

    double getMarginalLogLh();

    /**
     * @return <CODE>P(k|x)</CODE>
     */
    double[] getPosteriorProbs();
}
