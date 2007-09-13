package com.googlecode.array4j.io;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.MatrixTestSupport;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.packed.FloatPackedMatrix;
import net.lunglet.hdf.H5File;
import org.junit.Test;

public final class HDFWriterTest extends AbstractHDFTest {
    @Test
    public void testWriteFloatPackedMatrix() {
        H5File h5 = createMemoryH5File();
        HDFWriter writer = new HDFWriter(h5);
        HDFReader reader = new HDFReader(h5);
        int nmax = 5;
        for (int n = 1; n <= nmax; n++) {
            Storage storage = Storage.DIRECT;
            FloatPackedMatrix x = FloatPackedMatrix.createSymmetric(n, storage);
            MatrixTestSupport.populateMatrix(x);
            String name = String.valueOf(n);
            writer.write(x, name);
            FloatPackedMatrix y = FloatPackedMatrix.createSymmetric(n, storage);
            reader.read(y, name);
            assertEquals(x, y);
        }
        h5.close();
    }
}
