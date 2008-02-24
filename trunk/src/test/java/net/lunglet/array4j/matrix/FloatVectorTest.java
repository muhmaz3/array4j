package net.lunglet.array4j.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collection;
import net.lunglet.array4j.Storage;
import net.lunglet.array4j.math.FloatMatrixMath;
import net.lunglet.array4j.math.FloatMatrixUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public final class FloatVectorTest {
    private interface FloatVectorFactory {
    }

    @Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{{Storage.HEAP}, {Storage.DIRECT}});
    }

    private final Storage storage;

    public FloatVectorTest(final Storage storage) {
        this.storage = storage;
    }

    @Test
    public void testConstructors() {
//        factory.createVector(0, Direction.DEFAULT);
//        factory.createVector(1, Direction.DEFAULT);
//        factory.createVector(10, Direction.DEFAULT);
//        factory.createVector(new float[0], 0, 0, 1, Direction.ROW);
//        factory.createVector(new float[1], 1, 0, 1, Direction.ROW);
        // a really long constant vector
//        factory.createVector(new float[1], Integer.MAX_VALUE, 0, 0, Direction.ROW);
        // TODO test for overflows in checkArgument checks
    }

    @Test
    public void testFill() {
        for (int i = 0; i < 5; i++) {
//            FloatVector x = factory.createVector(i, Direction.DEFAULT);
            FloatVector x = null;
            for (int j = 0; j < i; j++) {
                assertEquals(0.0f, x.get(j), 0.0);
            }
            FloatMatrixUtils.fill(x, i);
            for (int j = 0; j < i; j++) {
                assertEquals(i, x.get(j), 0.0);
            }
        }
    }

    @Test
    public void testMinus() {
        for (int i = 0; i < 5; i++) {
//            FloatVector x = factory.createVector(i, Direction.DEFAULT);
//            FloatVector y = factory.createVector(i, Direction.DEFAULT);
            FloatVector x = null;
            FloatVector y = null;
            for (int j = 0; j < i; j++) {
                x.set(j, j);
                y.set(j, i - j);
            }
            FloatVector z = FloatMatrixMath.minus(x, y);
            for (int j = 0; j < i; j++) {
                assertEquals(j, x.get(j), 0.0);
                assertEquals(i - j, y.get(j), 0.0);
                assertEquals(2 * j - i, z.get(j), 0.0);
            }
        }
    }

    @Test
    public void testPlusEquals() {
        for (int i = 0; i < 5; i++) {
//            FloatVector x = factory.createVector(i, Direction.DEFAULT);
//            FloatVector y = factory.createVector(i, Direction.DEFAULT);
            FloatVector x = null;
            FloatVector y = null;
            for (int j = 0; j < i; j++) {
                x.set(j, j);
                y.set(j, i - j);
            }
            x.plusEquals(y);
            for (int j = 0; j < i; j++) {
                assertEquals(i, x.get(j), 0.0);
                assertEquals(i - j, y.get(j), 0.0);
            }
        }
    }

    @Test
    public void testIsRowColumnVector() {
        FloatVector v = null;
//        FloatVector v = factory.createVector(0, Direction.DEFAULT);
        assertTrue(v.isRowVector());
        assertTrue(v.isColumnVector());
//        v = factory.createVector(1, Direction.DEFAULT);
        assertTrue(v.isRowVector());
        assertTrue(v.isColumnVector());
//        v = factory.createVector(2, Direction.ROW);
        assertTrue(v.isRowVector());
        assertFalse(v.isColumnVector());
//        v = factory.createVector(2, Direction.COLUMN);
        assertFalse(v.isRowVector());
        assertTrue(v.isColumnVector());
    }

    @Test
    public void testTranspose() {
//        final FloatVector vector = factory.createVector(10, Direction.DEFAULT);
        final FloatVector vector = null;
        assertTrue("Vector must be a column vector by default", vector.isColumnVector());
        final FloatVector transposedVector = vector.transpose();
        assertTrue(transposedVector.isRowVector());
        assertFalse(vector.equals(transposedVector));
        final FloatVector originalVector = transposedVector.transpose();
        assertTrue(originalVector.isColumnVector());
        assertEquals(vector, originalVector);
    }
}
