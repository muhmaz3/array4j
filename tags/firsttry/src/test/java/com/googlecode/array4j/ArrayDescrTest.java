package com.googlecode.array4j;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class ArrayDescrTest {
    @Test
    public void testCanCastTo() {
        final ArrayDescr oldtype = ArrayDescr.fromType(ArrayType.DOUBLE);
        final ArrayDescr newtype = ArrayDescr.fromType(ArrayType.CDOUBLE);
        assertTrue(oldtype.canCastTo(newtype));
    }
}
