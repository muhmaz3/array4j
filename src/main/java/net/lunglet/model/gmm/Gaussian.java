package net.lunglet.model.gmm;

import net.lunglet.array4j.matrix.FloatVector;
import net.lunglet.model.Model;

public interface Gaussian extends Model {
    double logLikelihood(FloatVector x);

    Iterable<Double> logLikelihood(Iterable<? extends FloatVector> v);
}
