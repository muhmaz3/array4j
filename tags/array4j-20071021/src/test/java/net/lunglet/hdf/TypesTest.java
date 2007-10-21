package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class TypesTest {
    @Test
    public void testGetSize() {
        assertEquals(4L, FloatType.IEEE_F32LE.getSize());
        assertEquals(4L, IntType.STD_I32LE.getSize());
        assertEquals(8L, FloatType.IEEE_F64LE.getSize());
        assertEquals(1L, IntType.STD_I8BE.getSize());
    }

    @Test
    public void testPrefinedClose() {
        assertTrue(FloatType.IEEE_F32LE.getId() >= 0);
        FloatType.IEEE_F32LE.close();
        assertTrue(FloatType.IEEE_F32LE.getId() >= 0);
    }

    @Test
    public void testSetSize() {
        StringType stype = StringType.C_S1.copy();
        stype.setSize(123);
        assertEquals(123, stype.getSize());
    }
}
