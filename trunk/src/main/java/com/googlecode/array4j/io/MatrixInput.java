package com.googlecode.array4j.io;

import java.io.DataInput;
import java.io.IOException;

import com.googlecode.array4j.dense.FloatDenseMatrix;

public interface MatrixInput extends DataInput {
    FloatDenseMatrix readMatrix() throws IOException;
}
