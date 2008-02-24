package net.lunglet.io;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.dense.FloatDenseVector;

public interface MatrixOutput extends DataOutput {
    void writeColumnsAsMatrix(final Collection<? extends FloatDenseVector> columns) throws IOException;

    void writeMatrix(FloatMatrix matrix) throws IOException;
}
