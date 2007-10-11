package com.googlecode.array4j.io;

import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.packed.FloatPackedMatrix;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.DataType;
import net.lunglet.hdf.FloatType;
import net.lunglet.hdf.H5File;

// TODO handle any orientation, stride, offset, size, etc.

// TODO write via an intermediate direct buffer if matrix is
// stored on the heap and exceeds some maximum size

public final class HDFWriter {
    private final H5File h5file;

    public HDFWriter(final H5File h5file) {
        this.h5file = h5file;
    }

    public void close() {
        h5file.close();
    }

    public void write(final String name, final FloatDenseMatrix matrix) {
        if (!matrix.orientation().equals(Orientation.ROW)) {
            throw new IllegalArgumentException();
        }
        DataType memType = FloatType.IEEE_F32LE;
        DataSet dataset = h5file.getRootGroup().createDataSet(name, memType, matrix.rows(), matrix.columns());
        try {
            dataset.write(matrix.data(), memType);
        } finally {
            dataset.close();
        }
    }

    public void write(final String name, final FloatPackedMatrix matrix) {
        DataSet dataset = null;
        try {
            long n = matrix.rows();
            long k = n * (n + 1) / 2;
            DataType memType = FloatType.IEEE_F32LE;
            dataset = h5file.getRootGroup().createDataSet(name, memType, k);
            dataset.write(matrix.data(), memType);
        } finally {
            if (dataset != null) {
                dataset.close();
            }
        }
    }
}
