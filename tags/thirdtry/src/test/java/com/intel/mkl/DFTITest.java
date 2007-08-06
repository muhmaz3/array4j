package com.intel.mkl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import net.lunglet.mkl.fft.DftiException;

import org.junit.Ignore;
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
        double delta = 1.0e-6;

        DFTI.ComputeForward(handle, inout);
        assertEquals(6.0f, inout[0], delta);
        assertEquals(0.0f, inout[1], delta);
        assertEquals(-1.5f, inout[2], delta);
        assertEquals(0.8660254f, inout[3], delta);
        assertEquals(-1.5f, inout[4], delta);
        assertEquals(-0.8660254f, inout[5], delta);

        DFTI.ComputeBackward(handle, inout);
        assertEquals(1.0f, inout[0], delta);
        assertEquals(0.0f, inout[1], delta);
        assertEquals(2.0f, inout[2], delta);
        assertEquals(0.0f, inout[3], delta);
        assertEquals(3.0f, inout[4], delta);
        assertEquals(0.0f, inout[5], delta);

        DFTI.FreeDescriptor(handle);
    }

    @Ignore
    public void testSingleComplexOutOfPlace() throws DftiException {
        DFTI.DESCRIPTOR_HANDLE handle = DFTI.CreateDescriptor(DFTI.SINGLE, DFTI.COMPLEX, 1, 3);
        DFTI.SetValue(handle, DFTI.FORWARD_SCALE, 1.0f);
        DFTI.SetValue(handle, DFTI.PLACEMENT, DFTI.NOT_INPLACE);
        DFTI.CommitDescriptor(handle);
        float[] in = {1.0f, 2.0f, 3.0f};
        float[] out = new float[6];
        DFTI.ComputeForward(handle, in, out);
        // TODO these values are wrong
        System.out.println(Arrays.toString(out));
        DFTI.FreeDescriptor(handle);
    }
}
