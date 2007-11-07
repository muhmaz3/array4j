package com.googlecode.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class DirectionTest {
    @Test
    public void testSame() {
        assertTrue(Direction.ROW.same(Direction.ROW));
        assertTrue(Direction.ROW.same(Direction.BOTH));
        assertFalse(Direction.ROW.same(Direction.COLUMN));
        assertTrue(Direction.COLUMN.same(Direction.COLUMN));
        assertTrue(Direction.COLUMN.same(Direction.BOTH));
        assertFalse(Direction.COLUMN.same(Direction.ROW));
        assertTrue(Direction.BOTH.same(Direction.ROW));
        assertTrue(Direction.BOTH.same(Direction.COLUMN));
    }

    @Test
    public void testTranspose() {
        assertEquals(Direction.ROW, Direction.COLUMN.transpose());
        assertEquals(Direction.COLUMN, Direction.ROW.transpose());
        assertEquals(Direction.BOTH, Direction.BOTH.transpose());
    }
}
