package net.lunglet.io;

import com.googlecode.array4j.matrix.FloatMatrix;
import com.googlecode.array4j.matrix.FloatVector;
import com.googlecode.array4j.matrix.dense.FloatDenseVector;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

// TODO support little-endian output as well as big-endian

public final class MatrixOutputStream extends DataOutputStream implements MatrixOutput {
    public MatrixOutputStream(final OutputStream out) {
        // include a BufferedOutputStream here while the implementations use
        // primitive writes instead of writing larger blocks
        super(new BufferedOutputStream(out));
    }

    @Override
    public void writeColumnsAsMatrix(final Collection<? extends FloatDenseVector> columns) throws IOException {
        boolean wroteHeader = false;
        for (FloatDenseVector column : columns) {
            if (!wroteHeader) {
                // write number of rows
                writeInt(column.length());
                // write number of columns
                writeInt(columns.size());
                wroteHeader = true;
            }
            for (int i = 0; i < column.length(); i++) {
                writeFloat(column.get(i));
            }
        }
        // if there were no columns, write a header anyway
        if (!wroteHeader) {
            writeInt(0);
            writeInt(0);
        }
    }

    @Override
    public void writeMatrix(final FloatMatrix matrix) throws IOException {
        writeInt(matrix.rows());
        writeInt(matrix.columns());
        for (FloatVector column : matrix.columnsIterator()) {
            for (int i = 0; i < column.length(); i++) {
                writeFloat(column.get(i));
            }
        }
    }
}
