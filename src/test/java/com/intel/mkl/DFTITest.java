package com.intel.mkl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import net.lunglet.mkl.fft.DftiException;

import org.junit.Test;

public final class DFTITest {
    @Test
    public void testDefaultDescriptor() {
        DFTI.DESCRIPTOR_HANDLE handle = DFTI.CreateDescriptor(DFTI.SINGLE, DFTI.REAL, 1, 1);
        assertEquals(1, DFTI.GetValue(handle, DFTI.DIMENSION));
        DFTI.SetValue(handle, DFTI.DESCRIPTOR_NAME, "");
        assertEquals("", DFTI.GetValue(handle, DFTI.DESCRIPTOR_NAME));
        assertTrue(((String) DFTI.GetValue(handle, DFTI.VERSION)).startsWith("Intel"));
        assertEquals(DFTI.UNCOMMITTED, DFTI.GetValue(handle, DFTI.COMMIT_STATUS));
        assertEquals(1.0f, (Float) DFTI.GetValue(handle, DFTI.FORWARD_SCALE), 0.0);
        assertEquals(1.0f, (Float) DFTI.GetValue(handle, DFTI.BACKWARD_SCALE), 0.0);
        assertEquals(1, DFTI.GetValue(handle, DFTI.NUMBER_OF_TRANSFORMS));
        assertEquals(1, DFTI.GetValue(handle, DFTI.NUMBER_OF_USER_THREADS));
        assertEquals(0, DFTI.GetValue(handle, DFTI.INPUT_DISTANCE));
        assertEquals(0, DFTI.GetValue(handle, DFTI.OUTPUT_DISTANCE));
        assertEquals(DFTI.INPLACE, DFTI.GetValue(handle, DFTI.PLACEMENT));
        assertEquals(DFTI.COMPLEX_COMPLEX, DFTI.GetValue(handle, DFTI.COMPLEX_STORAGE));
        assertEquals(DFTI.REAL_REAL, DFTI.GetValue(handle, DFTI.REAL_STORAGE));
        assertEquals(DFTI.COMPLEX_REAL, DFTI.GetValue(handle, DFTI.CONJUGATE_EVEN_STORAGE));
        assertEquals(DFTI.CCS_FORMAT, DFTI.GetValue(handle, DFTI.PACKED_FORMAT));
        assertEquals(DFTI.ORDERED, DFTI.GetValue(handle, DFTI.ORDERING));
        assertEquals(DFTI.NONE, DFTI.GetValue(handle, DFTI.TRANSPOSE));
        assertEquals(1, DFTI.GetValue(handle, DFTI.LENGTHS));
        assertTrue(Arrays.equals(new int[]{0, 1}, (int[]) DFTI.GetValue(handle, DFTI.INPUT_STRIDES)));
        assertTrue(Arrays.equals(new int[]{0, 1}, (int[]) DFTI.GetValue(handle, DFTI.OUTPUT_STRIDES)));
        DFTI.CommitDescriptor(handle);
        assertEquals(DFTI.COMMITTED, DFTI.GetValue(handle, DFTI.COMMIT_STATUS));
        DFTI.FreeDescriptor(handle);
    }

    @Test
    public void testSingleComplexInPlace() throws DftiException {
        DFTI.DESCRIPTOR_HANDLE handle = DFTI.CreateDescriptor(DFTI.SINGLE, DFTI.COMPLEX, 1, 3);
        DFTI.SetValue(handle, DFTI.FORWARD_SCALE, 1.0f);
        DFTI.SetValue(handle, DFTI.BACKWARD_SCALE, 1.0f / 3);
        DFTI.CommitDescriptor(handle);
        float[] inout = {1.0f, 0.0f, 2.0f, 0.0f, 3.0f, 0.0f};
        DFTI.ComputeForward(handle, inout);
//        System.out.println(Arrays.toString(inout));
        DFTI.ComputeBackward(handle, inout);
//        System.out.println(Arrays.toString(inout));
        DFTI.FreeDescriptor(handle);
    }
}
