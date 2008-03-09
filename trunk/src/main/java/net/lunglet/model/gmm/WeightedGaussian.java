package net.lunglet.model.gmm;

public interface WeightedGaussian extends Gaussian {
    double getLogWeight();

    double getWeight();
}
