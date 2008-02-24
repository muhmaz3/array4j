package com.googlecode.array4j.io;

import com.googlecode.array4j.matrix.dense.FloatDenseMatrix;
import java.io.DataInput;
import java.io.IOException;

public interface MatrixInput extends DataInput {
    FloatDenseMatrix readMatrix() throws IOException;
}
