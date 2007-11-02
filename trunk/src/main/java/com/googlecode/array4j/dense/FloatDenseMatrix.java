package com.googlecode.array4j.dense;

import com.googlecode.array4j.FloatMatrix;
import java.nio.FloatBuffer;
import java.util.List;

public interface FloatDenseMatrix extends FloatMatrix, DenseMatrix {
    /** {@inheritDoc} */
    FloatDenseVector asVector();

    /** {@inheritDoc} */
    FloatDenseVector column(int column);

    /** {@inheritDoc} */
    Iterable<FloatDenseVector> columnsIterator();

    /** {@inheritDoc} */
    List<FloatDenseVector> columnsList();

    /** {@inheritDoc} */
    FloatBuffer data();

    /** {@inheritDoc} */
    FloatDenseVector row(int row);

    /** {@inheritDoc} */
    Iterable<FloatDenseVector> rowsIterator();

    /** {@inheritDoc} */
    FloatDenseMatrix transpose();
}
