package net.lunglet.io;

import static org.junit.Assert.assertEquals;
import net.lunglet.array4j.matrix.MatrixTestSupport;
import net.lunglet.array4j.matrix.packed.FloatPackedMatrix;
import net.lunglet.array4j.matrix.packed.PackedFactory;
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
            FloatPackedMatrix x = PackedFactory.floatSymmetricDirect(n);
            MatrixTestSupport.populateMatrix(x);
            String name = String.valueOf(n);
            writer.write(name, x);
            FloatPackedMatrix y = PackedFactory.floatSymmetricDirect(n);
            reader.read(name, y);
            assertEquals(x, y);
        }
        h5.close();
    }
}
