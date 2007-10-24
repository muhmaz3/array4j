package com.googlecode.array4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class OrientationTest {
    @Test
    public void testSame() {
        assertTrue(Order.ROW.same(Order.ROW));
        assertTrue(Order.COLUMN.same(Order.COLUMN));
        assertFalse(Order.ROW.same(Order.COLUMN));
        assertFalse(Order.COLUMN.same(Order.ROW));
        assertFalse(Order.ROW.same(new Order[]{null}));
    }
}
