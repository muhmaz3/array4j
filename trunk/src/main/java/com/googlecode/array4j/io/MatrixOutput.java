package com.googlecode.array4j.io;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;

public interface MatrixOutput extends DataOutput {
    void writeColumnsAsMatrix(final Collection<? extends FloatDenseVector> columns) throws IOException;

    void writeMatrix(FloatMatrix<?, ?> matrix) throws IOException;
}
