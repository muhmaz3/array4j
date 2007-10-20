package com.googlecode.array4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class OrientationTest {
    @Test
    public void testSame() {
        assertTrue(Orientation.ROW.same(Orientation.ROW));
        assertTrue(Orientation.COLUMN.same(Orientation.COLUMN));
        assertFalse(Orientation.ROW.same(Orientation.COLUMN));
        assertFalse(Orientation.COLUMN.same(Orientation.ROW));
        assertFalse(Orientation.ROW.same(new Orientation[]{null}));
    }
}
