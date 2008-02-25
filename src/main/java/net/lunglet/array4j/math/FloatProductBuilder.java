package net.lunglet.array4j.math;

import net.lunglet.array4j.matrix.FloatMatrix;

public final class FloatProductBuilder extends AbstractProductBuilder {
    public FloatMatrix build() {
        return null;
    }

    public FloatProductBuilder times(final float value) {
        return this;
    }

    public FloatProductBuilder times(final FloatMatrix matrix) {
        return this;
    }
}
