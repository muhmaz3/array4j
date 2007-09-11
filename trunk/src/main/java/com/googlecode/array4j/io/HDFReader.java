package com.googlecode.array4j.io;

import com.googlecode.array4j.packed.FloatPackedMatrix;
import java.io.IOException;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.DataSpace;
import net.lunglet.hdf.H5File;
import net.lunglet.hdf.PredefinedType;

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
            // TODO read into an intermediate direct buffer if matrix exceeds
            // some maximum size
            dataset.read(matrix.data(), PredefinedType.IEEE_F32LE);
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
