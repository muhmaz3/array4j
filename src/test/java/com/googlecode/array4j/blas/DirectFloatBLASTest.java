package com.googlecode.array4j.blas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.array4j.DirectFloatVector;

public final class DirectFloatBLASTest {
    @Test
    public void test() {
        DirectFloatVector x = new DirectFloatVector(1.0f, 2.0f, 3.0f);
        DirectFloatVector y = new DirectFloatVector(3.0f, 2.0f, 1.0f);
        assertEquals(10.0f, DirectFloatBLAS.dot(x, y));
    }
}
