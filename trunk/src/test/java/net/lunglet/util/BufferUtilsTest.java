package net.lunglet.util;

import static org.junit.Assert.assertEquals;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.junit.Test;

public final class BufferUtilsTest {
    @Test
    public void testGetBytesCapacity() {
        assertEquals(1, BufferUtils.getBytesCapacity(ByteBuffer.allocate(1)));
        assertEquals(1, BufferUtils.getBytesCapacity(ByteBuffer.allocateDirect(1)));
        assertEquals(4, BufferUtils.getBytesCapacity(FloatBuffer.allocate(1)));
        assertEquals(4, BufferUtils.getBytesCapacity(ByteBuffer.allocate(4).asFloatBuffer()));
        assertEquals(4, BufferUtils.getBytesCapacity(ByteBuffer.allocateDirect(4).asFloatBuffer()));
    }
}
