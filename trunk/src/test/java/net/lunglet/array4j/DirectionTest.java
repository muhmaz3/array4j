package net.lunglet.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class DirectionTest {
    @Test
    public void testSame() {
        assertTrue(Direction.ROW.same(Direction.ROW));
        assertFalse(Direction.ROW.same(Direction.COLUMN));
        assertTrue(Direction.COLUMN.same(Direction.COLUMN));
        assertFalse(Direction.COLUMN.same(Direction.ROW));
    }

    @Test
    public void testTranspose() {
        assertEquals(Direction.ROW, Direction.COLUMN.transpose());
        assertEquals(Direction.COLUMN, Direction.ROW.transpose());
    }
}
