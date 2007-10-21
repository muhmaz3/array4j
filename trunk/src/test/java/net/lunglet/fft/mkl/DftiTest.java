package net.lunglet.fft.mkl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import org.junit.Ignore;
import org.junit.Test;

public final class DftiTest {
    protected static FloatBuffer createFloatBuffer(final int size) {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(size * (Float.SIZE >>> 3));
        buffer.order(ByteOrder.nativeOrder());
        return buffer.asFloatBuffer();
    }

    @Ignore
    @Test
    public void testDescriptorConstructor() throws DftiException {
        final int[] lengths = new int[]{1};
        DftiDescriptor desc = new DftiDescriptor(DftiConfigValue.SINGLE, DftiConfigValue.REAL, lengths);
        assertEquals(lengths.length, desc.getIntValue(DftiConfigParam.DIMENSION));
//        assertEquals("", desc.getStringValue(DftiConfigParam.DESCRIPTOR_NAME));
        assertTrue(desc.getStringValue(DftiConfigParam.VERSION).startsWith("Intel"));
        assertEquals(DftiConfigValue.UNCOMMITTED, desc.getValue(DftiConfigParam.COMMIT_STATUS));
        assertEquals(1.0f, desc.getFloatValue(DftiConfigParam.FORWARD_SCALE), 0.0);
        assertEquals(1.0f, desc.getFloatValue(DftiConfigParam.BACKWARD_SCALE), 0.0);
        assertEquals(1, desc.getIntValue(DftiConfigParam.NUMBER_OF_TRANSFORMS));
        assertEquals(1, desc.getIntValue(DftiConfigParam.NUMBER_OF_USER_THREADS));
        assertEquals(0, desc.getIntValue(DftiConfigParam.INPUT_DISTANCE));
        assertEquals(0, desc.getIntValue(DftiConfigParam.OUTPUT_DISTANCE));
        assertEquals(DftiConfigValue.INPLACE, desc.getValue(DftiConfigParam.PLACEMENT));
        assertEquals(DftiConfigValue.COMPLEX_COMPLEX, desc.getValue(DftiConfigParam.COMPLEX_STORAGE));
        assertEquals(DftiConfigValue.REAL_REAL, desc.getValue(DftiConfigParam.REAL_STORAGE));
        assertEquals(DftiConfigValue.COMPLEX_REAL, desc.getValue(DftiConfigParam.CONJUGATE_EVEN_STORAGE));
        assertEquals(DftiConfigValue.CCS_FORMAT, desc.getValue(DftiConfigParam.PACKED_FORMAT));
        assertEquals(DftiConfigValue.ORDERED, desc.getValue(DftiConfigParam.ORDERING));
        assertEquals(DftiConfigValue.NONE, desc.getValue(DftiConfigParam.TRANSPOSE));
        assertTrue(Arrays.equals(new int[]{1}, desc.getIntArrayValue(DftiConfigParam.LENGTHS)));
        assertTrue(Arrays.equals(new int[]{0, 1}, desc.getIntArrayValue(DftiConfigParam.INPUT_STRIDES)));
        assertTrue(Arrays.equals(new int[]{0, 1}, desc.getIntArrayValue(DftiConfigParam.OUTPUT_STRIDES)));
        desc.commit();
        assertEquals(DftiConfigValue.COMMITTED, desc.getValue(DftiConfigParam.COMMIT_STATUS));
        desc.free();
    }

    @Ignore
    @Test
    public void testDescriptorConstructorStress() throws DftiException {
        for (int i = 0; i < 100000; i++) {
            testDescriptorConstructor();
        }
    }

    @Ignore
    @Test
    public void testDescriptorSetValue() throws DftiException {
        DftiDescriptor desc = new DftiDescriptor(DftiConfigValue.SINGLE, DftiConfigValue.REAL, new int[]{1});
//        desc.setValue(DftiConfigParam.DESCRIPTOR_NAME, "hello");
//        assertEquals("hello", desc.getStringValue(DftiConfigParam.DESCRIPTOR_NAME));
        desc.commit();
        desc.free();
    }

    @Ignore
    @Test
    public void testSingleComplexInPlace() throws DftiException {
        final int[] lengths = new int[]{3};
        DftiDescriptor desc = new DftiDescriptor(DftiConfigValue.SINGLE, DftiConfigValue.COMPLEX, lengths);
        desc.setValue(DftiConfigParam.FORWARD_SCALE, 1.0f);
        desc.setValue(DftiConfigParam.BACKWARD_SCALE, 1.0f / lengths[0]);
        desc.commit();
        FloatBuffer inout = createFloatBuffer(6);
        inout.put(0, 1.0f);
        inout.put(2, 2.0f);
        inout.put(4, 3.0f);

        desc.computeForward(inout);
        double delta = 1.0e-6;
        assertEquals(6.0f, inout.get(0), delta);
        assertEquals(0.0f, inout.get(1), delta);
        assertEquals(-1.5f, inout.get(2), delta);
        assertEquals(0.8660254f, inout.get(3), delta);
        assertEquals(-1.5f, inout.get(4), delta);
        assertEquals(-0.8660254f, inout.get(5), delta);

        desc.computeBackward(inout);
        assertEquals(1.0f, inout.get(0), delta);
        assertEquals(0.0f, inout.get(1), delta);
        assertEquals(2.0f, inout.get(2), delta);
        assertEquals(0.0f, inout.get(3), delta);
        assertEquals(3.0f, inout.get(4), delta);
        assertEquals(0.0f, inout.get(5), delta);

        desc.free();
    }

    @Ignore
    @Test
    public void testSingleComplexOutOfPlace() throws DftiException {
        final int[] lengths = new int[]{3};
        DftiDescriptor desc = new DftiDescriptor(DftiConfigValue.SINGLE, DftiConfigValue.REAL, lengths);
        desc.setValue(DftiConfigParam.FORWARD_SCALE, 1.0f);
        desc.setValue(DftiConfigParam.BACKWARD_SCALE, 1.0f / lengths[0]);
        desc.setValue(DftiConfigParam.PLACEMENT, DftiConfigValue.NOT_INPLACE);
        desc.setValue(DftiConfigParam.INPUT_STRIDES, new int[]{1, 1});
        desc.setValue(DftiConfigParam.OUTPUT_STRIDES, new int[]{1, 1});
        desc.commit();
        FloatBuffer in = createFloatBuffer(4);
        in.put(1, 1.0f);
        in.put(2, 2.0f);
        in.put(3, 3.0f);
        FloatBuffer out = createFloatBuffer(7);
        desc.computeForward(in, out);
        double delta = 1.0e-6;
        assertEquals(6.0f, out.get(1), delta);
        assertEquals(0.0f, out.get(2), delta);
        assertEquals(-1.5f, out.get(3), delta);
        assertEquals(0.8660254f, out.get(4), delta);
//        assertEquals(-1.5f, out.get(5), delta);
//        assertEquals(-0.8660254f, out.get(6), delta);
        desc.free();
    }
}
