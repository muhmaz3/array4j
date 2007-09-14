package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class TypesTest {
    @Test
    public void testGetSize() {
        assertEquals(4, FloatType.IEEE_F32LE.getSize());
        assertEquals(4, IntType.STD_I32LE.getSize());
        assertEquals(8, FloatType.IEEE_F64LE.getSize());
        assertEquals(1, IntType.STD_I8BE.getSize());
    }

    @Test
    public void testSetSize() {
        StringType stype = StringType.C_S1.copy();
        stype.setSize(123);
        assertEquals(123, stype.getSize());
    }
}
