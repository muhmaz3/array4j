package com.googlecode.array4j.io;

import com.googlecode.array4j.packed.FloatPackedMatrix;
import java.io.IOException;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.DataSpace;
import net.lunglet.hdf.FloatType;
import net.lunglet.hdf.H5File;

// TODO handle any orientation, stride, offset, size, etc.

// TODO read via an intermediate direct buffer if matrix is
// stored on the heap and exceeds some maximum size

public final class HDFReader {
    private final H5File h5file;

    public HDFReader(final H5File h5file) {
        this.h5file = h5file;
    }

    public void close() {
        h5file.close();
    }

    public void read(final FloatPackedMatrix matrix, final String name) {
        DataSet dataset = null;
        DataSpace space = null;
        try {
            dataset = h5file.getRootGroup().openDataSet(name);
            space = dataset.getSpace();
            long[] dims = space.getDims();
            if (dims.length != 1) {
                throw new RuntimeException(new IOException());
            }
            int k = (int) dims[0];
            int n = ((int) Math.sqrt(1 + 8 * k) - 1) / 2;
            if (matrix.rows() != n) {
                throw new IllegalArgumentException();
            }
            dataset.read(matrix.data(), FloatType.IEEE_F32LE);
        } finally {
            if (space != null) {
                space.close();
            }
            if (dataset != null) {
                dataset.close();
            }
        }
    }
}
