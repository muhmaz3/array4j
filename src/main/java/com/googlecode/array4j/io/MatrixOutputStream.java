package com.googlecode.array4j.io;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class MatrixOutputStream extends DataOutputStream implements MatrixOutput {
    public MatrixOutputStream(final OutputStream out) {
        // include a BufferedOutputStream here while the implementations use
        // primitive writes instead of writing larger blocks
        super(new BufferedOutputStream(out));
    }

    @Override
    public void writeMatrix(final FloatMatrix<?, ?> matrix) throws IOException {
        writeInt(matrix.rows());
        writeInt(matrix.columns());
        for (FloatVector<?> column : matrix.columnsIterator()) {
            for (int i = 0; i < column.size(); i++) {
                writeFloat(column.get(i));
            }
        }
    }

    @Override
    public void writeColumnsAsMatrix(final Collection<? extends FloatDenseVector> columns) throws IOException {
        writeInt(columns.size());
        boolean wroteColumns = false;
        for (FloatDenseVector column : columns) {
            if (!wroteColumns) {
                writeInt(column.size());
                wroteColumns = true;
            }
            for (int i = 0; i < column.size(); i++) {
                writeFloat(column.get(i));
            }
        }
    }
}
