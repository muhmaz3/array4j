package net.lunglet.array4j.matrix.packed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.lunglet.array4j.matrix.MatrixTestSupport;
import org.junit.Test;

public final class FloatPackedMatrixTest {
    private void checkRowsColumns(final FloatPackedMatrix x) {
        for (int i = 0; i < x.rows(); i++) {
            for (int j = 0; j < x.columns(); j++) {
                assertEquals(x.get(i, j), x.row(i).get(j), 0);
                assertEquals(x.get(i, j), x.column(j).get(i), 0);
            }
        }
    }

    private FloatPackedMatrix serialize(final FloatPackedMatrix a) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(a);
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            return (FloatPackedMatrix) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLowerTriangular() {
        FloatPackedMatrix tril = PackedFactory.floatLowerTriangular(2, 2);
        assertTrue(tril.isLowerTriangular());
        tril.set(0, 0, 1.0f);
        tril.set(1, 0, 2.0f);
        tril.set(1, 1, 3.0f);
        try {
            tril.set(0, 1, 4.0f);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        assertEquals(1.0f, tril.get(0, 0), 0);
        assertEquals(0.0f, tril.get(0, 1), 0);
        assertEquals(2.0f, tril.get(1, 0), 0);
        assertEquals(3.0f, tril.get(1, 1), 0);
        assertEquals(2.0f, tril.transpose().get(0, 1), 0);
        checkRowsColumns(tril);
        FloatPackedMatrix tril2 = serialize(tril);
        MatrixTestSupport.checkMatrix(tril, tril2, 0);
    }

    @Test
    public void testSymmetric() {
        FloatPackedMatrix symm = PackedFactory.floatSymmetric(2);
        assertTrue(symm.isSymmetric());
        symm.set(0, 0, 1.0f);
        symm.set(0, 1, 2.0f);
        symm.set(1, 1, 3.0f);
        assertEquals(1.0f, symm.get(0, 0), 0);
        assertEquals(2.0f, symm.get(0, 1), 0);
        assertEquals(2.0f, symm.get(1, 0), 0);
        assertEquals(3.0f, symm.get(1, 1), 0);
        symm.set(1, 0, 4.0f);
        assertEquals(4.0f, symm.get(0, 1), 0);
        assertEquals(4.0f, symm.get(1, 0), 0);
        checkRowsColumns(symm);
        FloatPackedMatrix symm2 = serialize(symm);
        MatrixTestSupport.checkMatrix(symm, symm2, 0);
    }

    @Test
    public void testUpperTriangular() {
        FloatPackedMatrix triu = PackedFactory.floatUpperTriangular(2, 2);
        assertTrue(triu.isUpperTriangular());
        triu.set(0, 0, 1.0f);
        triu.set(0, 1, 2.0f);
        triu.set(1, 1, 3.0f);
        try {
            triu.set(1, 0, 4.0f);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        assertEquals(1.0f, triu.get(0, 0), 0);
        assertEquals(2.0f, triu.get(0, 1), 0);
        assertEquals(0.0f, triu.get(1, 0), 0);
        assertEquals(3.0f, triu.get(1, 1), 0);
        assertEquals(2.0f, triu.transpose().get(1, 0), 0);
        checkRowsColumns(triu);
        FloatPackedMatrix triu2 = serialize(triu);
        MatrixTestSupport.checkMatrix(triu, triu2, 0);
    }
}
