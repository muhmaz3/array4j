package net.lunglet.array4j.matrix.dense;

import net.lunglet.array4j.matrix.Vector;

public interface DenseVector extends DenseMatrix, Vector {
    /** {@inheritDoc} */
    DenseVector transpose();
}
