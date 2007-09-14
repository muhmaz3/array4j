package com.googlecode.array4j.io;

import static org.junit.Assert.assertEquals;
import com.googlecode.array4j.MatrixTestSupport;
import com.googlecode.array4j.packed.FloatPackedMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.lunglet.hdf.DataSet;
import net.lunglet.hdf.DataSpace;
import net.lunglet.hdf.DataType;
import net.lunglet.hdf.FloatType;
import net.lunglet.hdf.Group;
import net.lunglet.hdf.H5File;
import net.lunglet.hdf.Point;
import net.lunglet.hdf.SelectionOperator;
import org.junit.Test;

public final class HDFReaderTest extends AbstractHDFTest {
    private static long elementOffset(final long m, final long n) {
        if (m > n) {
            throw new IllegalArgumentException();
        }
        // calculate offset for upper triangular entry
        return m + (n + 1L) * n / 2L;
    }

    @Test
    public void testReadFloatPackedMatrix() {
        H5File h5 = createMemoryH5File();
        Group group = h5.getRootGroup().createGroup("/foo");
        group.close();
        DataSet ds = h5.getRootGroup().createDataSet("/foo/bar", FloatType.IEEE_F32LE, 3);
        ds.write(new float[]{1.0f, 2.0f, 3.0f});
        ds.close();
        HDFReader reader = new HDFReader(h5);
        FloatPackedMatrix x = FloatPackedMatrix.createSymmetric(2);
        reader.read(x, "/foo/bar");
        reader.close();
        assertEquals(1.0f, x.get(0, 0), 0);
        assertEquals(2.0f, x.get(0, 1), 0);
        assertEquals(2.0f, x.get(1, 0), 0);
        assertEquals(3.0f, x.get(1, 1), 0);
    }

    @Test
    public void testReadPackedRowsColumns() {
        H5File h5 = createMemoryH5File();
        DataType dtype = FloatType.IEEE_F32LE;
        int n = 5;
        int k = n * (n + 1) / 2;
        DataSet ds = h5.getRootGroup().createDataSet("data", dtype, k);
        float[] values = new float[k];
        for (int i = 0; i < values.length; i++) {
            values[i] = i + 1.0f;
        }
        ds.write(values);
        int[] indexes = new int[n];
        Random rng = new Random(0);
        int len = 0;
        for (int i = 0; i < n; i++) {
            if (rng.nextBoolean()) {
                indexes[len++] = i;
            }
        }
        indexes = Arrays.copyOf(indexes, len);

        // XXX read code starts here
        FloatPackedMatrix x = FloatPackedMatrix.createSymmetric(indexes.length);
        int size = indexes.length * (indexes.length + 1) / 2;
        DataSpace memSpace = new DataSpace(size);
        memSpace.selectAll();
        DataSpace fileSpace = ds.getSpace();
        List<Point> pointsList = new ArrayList<Point>();
        for (int i = 0; i < indexes.length; i++) {
            for (int j = i; j < indexes.length; j++) {
                Point point = new Point(elementOffset(indexes[i], indexes[j]));
                pointsList.add(point);
            }
        }
        fileSpace.selectElements(SelectionOperator.SET, pointsList.toArray(new Point[0]));
        ds.read(x.data(), dtype, memSpace, fileSpace);
        memSpace.close();
        fileSpace.close();
        ds.close();
        h5.close();
        // TODO need to check that x contains the right values
    }

    @Test
    public void testXXX() {
        FloatPackedMatrix x = FloatPackedMatrix.createSymmetric(4);
        MatrixTestSupport.populateMatrix(x);
        int[] indexes = {0, 2, 1};
        FloatPackedMatrix y = FloatPackedMatrix.createSymmetric(indexes.length);
        for (int i = 0; i < indexes.length; i++) {
            for (int j = i; j < indexes.length; j++) {
                y.set(i, j, x.get(indexes[i], indexes[j]));
            }
        }
        System.out.println(x);
        System.out.println(y);
    }
}
