package net.lunglet.model.gmm;

import java.util.Iterator;
import net.lunglet.array4j.matrix.FloatVector;

public abstract class AbstractGaussian {
    public abstract int getDimension();

    public abstract double logLikelihood(FloatVector x);

    protected final void checkInput(final FloatVector x) {
        if (x.length() != getDimension()) {
            throw new IllegalArgumentException();
        }
    }

    public final Iterable<Double> logLikelihood(final Iterable<? extends FloatVector> v) {
        final Iterator<? extends FloatVector> it = v.iterator();
        return new Iterable<Double>() {
            @Override
            public Iterator<Double> iterator() {
                return new Iterator<Double>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Double next() {
                        return logLikelihood(it.next());
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
