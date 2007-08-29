package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class PredefinedTypeTest {
    @Test
    public void testGetSize() {
        assertEquals(4, PredefinedType.IEEE_F32LE.getSize());
        assertEquals(4, PredefinedType.STD_I32LE.getSize());
        assertEquals(8, PredefinedType.IEEE_F64LE.getSize());
        assertEquals(1, PredefinedType.STD_I8BE.getSize());
    }
}
