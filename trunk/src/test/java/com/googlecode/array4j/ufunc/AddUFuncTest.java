package com.googlecode.array4j.ufunc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.googlecode.array4j.DenseArray;
import com.googlecode.array4j.DoubleArray;

public final class AddUFuncTest {
    @Test
    public void testCall() {
    }

    @Test
    public void testAccumulate() {
        final UFunc add = UFuncs.ADD;

        final DenseArray in = DoubleArray.arange(5.0);
        DenseArray out;

        out = add.accumulate(in, 0, null, null);
        assertNotNull(out);
        assertEquals(0.0, out.getDouble(0));
        assertEquals(1.0, out.getDouble(1));
        assertEquals(3.0, out.getDouble(2));
        assertEquals(6.0, out.getDouble(3));
        assertEquals(10.0, out.getDouble(4));
    }

    @Test
    public void testReduce() {
    }

    @Test
    public void testReduceAt() {
    }

    @Test
    public void testOuter() {
    }
}
