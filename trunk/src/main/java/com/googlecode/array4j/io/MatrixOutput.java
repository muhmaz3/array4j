package com.googlecode.array4j.io;

import com.googlecode.array4j.matrix.FloatMatrix;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

public interface MatrixOutput extends DataOutput {
    void writeColumnsAsMatrix(final Collection<? extends FloatDenseVector> columns) throws IOException;

    void writeMatrix(FloatMatrix matrix) throws IOException;
}
