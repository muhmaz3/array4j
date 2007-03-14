package com.googlecode.array4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class ArrayTypeTest {
    @Test
    public void testIsInteger() {
        assertFalse(ArrayType.BOOL.isInteger());
        assertTrue(ArrayType.BYTE.isInteger());
        assertTrue(ArrayType.SHORT.isInteger());
        assertTrue(ArrayType.INT.isInteger());
        assertTrue(ArrayType.LONG.isInteger());
        assertFalse(ArrayType.FLOAT.isInteger());
        assertFalse(ArrayType.DOUBLE.isInteger());
    }
}
