package com.googlecode.array4j.io;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.MatrixTestSupport;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.matrix.packed.FloatPackedMatrix;
import com.googlecode.array4j.matrix.packed.PackedFactory;
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
            FloatPackedMatrix x = PackedFactory.createFloatSymmetric(n, storage);
            MatrixTestSupport.populateMatrix(x);
            String name = String.valueOf(n);
            writer.write(name, x);
            FloatPackedMatrix y = PackedFactory.createFloatSymmetric(n, storage);
            reader.read(name, y);
            assertEquals(x, y);
        }
        h5.close();
    }
}
