package com.googlecode.array4j.gmm;

import com.googlecode.array4j.Array;
import com.googlecode.array4j.Vector;

public interface Gaussian {
    Vector logLikelihood(final Array data);

    double logLikelihood(final double... values);

    int getDimension();
}
