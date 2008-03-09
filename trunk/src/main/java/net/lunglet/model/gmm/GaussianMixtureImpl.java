package net.lunglet.model.gmm;

import java.util.Iterator;
import net.lunglet.array4j.matrix.FloatVector;

public final class GaussianMixtureImpl extends AbstractGaussian implements GaussianMixture {
    @Override
    public int getDimension() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double logLikelihood(final FloatVector x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Gaussian> iterator() {
        return new Iterator<Gaussian>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Gaussian next() {
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
