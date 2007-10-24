package com.googlecode.array4j.io;

import com.googlecode.array4j.dense.FloatDenseMatrix;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class MatrixInputStream extends DataInputStream implements MatrixInput {

    public MatrixInputStream(final InputStream in) {
        super(new BufferedInputStream(in));
    }

    @Override
    public FloatDenseMatrix readMatrix() throws IOException {
        int rows = readInt();
        if (rows < 0) {
            throw new IOException();
        }
        int columns = readInt();
        if (columns < 0) {
            throw new IOException();
        }
//        FloatDenseMatrix matrix = new FloatDenseMatrix(rows, columns);
        FloatDenseMatrix matrix = null;
        // read matrix in column-major order
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                matrix.set(i, j, readFloat());
            }
        }
        return matrix;
    }
}
