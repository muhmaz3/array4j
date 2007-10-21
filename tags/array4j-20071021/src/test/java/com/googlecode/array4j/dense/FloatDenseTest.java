package com.googlecode.array4j.dense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.MatrixTestSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

public final class FloatDenseTest {
    private void checkSerialization(final FloatMatrix<?, ?> x) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(x);
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        FloatMatrix<?, ?> y = (FloatMatrix<?, ?>) ois.readObject();
        ois.close();
        assertEquals(x.getClass(), y.getClass());
        assertTrue(Arrays.equals(x.shape(), y.shape()));
        for (int i = 0; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++) {
                assertEquals(x.get(i, j), y.get(i, j), 0);
            }
        }
        if (x instanceof DenseMatrix) {
            DenseMatrix<?, ?> dx = (DenseMatrix<?, ?>) x;
            DenseMatrix<?, ?> dy = (DenseMatrix<?, ?>) y;
            assertEquals(dx.data().isDirect(), dy.data().isDirect());
            assertEquals(dx.storage(), dy.storage());
            assertEquals(dx.orientation(), dy.orientation());
        }
    }

    @Test
    public void testAsMatrix() {
        FloatDenseMatrix x = new FloatDenseMatrix(2, 3);
        MatrixTestSupport.populateMatrix(x);
        for (int i = 0; i < x.columns(); i++) {
            FloatDenseMatrix y = x.column(i).asMatrix();
            FloatDenseMatrix z = x.column(i).transpose().asMatrix();
            for (int j = 0; j < x.rows(); j++) {
                assertEquals(y.get(j), x.get(j, i), 0);
                assertEquals(y.get(j, 0), x.get(j, i), 0);
                assertEquals(z.get(j), x.get(j, i), 0);
                assertEquals(z.get(0, j), x.get(j, i), 0);
            }
        }
    }

    @Ignore
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        List<FloatMatrix<?, ?>> matrices = new ArrayList<FloatMatrix<?, ?>>();
        matrices.add(new FloatDenseMatrix(0, 0));
        matrices.add(new FloatDenseMatrix(1, 1));
        matrices.add(new FloatDenseMatrix(2, 3));
        matrices.add(new FloatDenseVector(0));
        matrices.add(new FloatDenseVector(1));
        for (FloatVector<?> v : new FloatDenseMatrix(3, 4).columnsIterator()) {
            matrices.add(v);
        }
        for (FloatVector<?> v : new FloatDenseMatrix(3, 4).rowsIterator()) {
            matrices.add(v);
        }
        for (FloatMatrix<?, ?> x : matrices) {
            MatrixTestSupport.populateMatrix(x);
            checkSerialization(x);
        }
    }
}
