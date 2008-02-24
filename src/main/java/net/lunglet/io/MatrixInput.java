package net.lunglet.io;

import java.io.DataInput;
import java.io.IOException;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;

public interface MatrixInput extends DataInput {
    FloatDenseMatrix readMatrix() throws IOException;
}
