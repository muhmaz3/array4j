package com.googlecode.array4j.ufunc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ErrorTest {
    @Test
    public void testDefaultErrorMask() {
        assertEquals(2084, Error.DEFAULT_ERROR.getErrorMask());
    }
}
