package net.lunglet.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.sun.jna.NativeLong;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.lunglet.array4j.Storage;
import net.lunglet.util.BufferUtils.BufferUtilsLibrary;
import org.junit.Test;

// TODO test createAlignedBuffer on a 64-bit system

public final class BufferUtilsTest {
    private static Buffer checkAlignedBuffer(final int size, final int alignment) {
        BufferUtilsLibrary lib = BufferUtils.BufferUtilsLibrary.INSTANCE;
        ByteBuffer buffer = BufferUtils.createAlignedBuffer(size, alignment);
        NativeLong addr = lib.array4j_addressof(buffer);
        assertEquals(0, addr.longValue() % alignment);
        assertEquals(size, buffer.position(0).remaining());
        assertTrue(buffer.capacity() >= size);
        return buffer;
    }

    @Test
    public void testByteOrder() {
        assertEquals(ByteOrder.nativeOrder(), BufferUtils.createComplexFloatBuffer(1, Storage.DIRECT).order());
        assertEquals(ByteOrder.nativeOrder(), BufferUtils.createDoubleBuffer(1, Storage.DIRECT).order());
        assertEquals(ByteOrder.nativeOrder(), BufferUtils.createFloatBuffer(1, Storage.DIRECT).order());
        assertEquals(ByteOrder.nativeOrder(), BufferUtils.createAlignedBuffer(1, 1).order());
    }

    @Test
    public void testCreateAlignedBuffer() {
        // keep references to buffers to ensure new allocations
        List<Buffer> buffers = new ArrayList<Buffer>();
        for (int i = 0; i < 5; i++) {
            for (int size = 0; size < 128; size++) {
                for (int alignment = 1; alignment < 64; alignment++) {
                    Buffer buffer = checkAlignedBuffer(size, alignment);
                    buffers.add(buffer);
                }
            }
        }
    }

    @Test
    public void testGetBytesCapacity() {
        assertEquals(1, BufferUtils.getBytesCapacity(ByteBuffer.allocate(1)));
        assertEquals(1, BufferUtils.getBytesCapacity(ByteBuffer.allocateDirect(1)));
        assertEquals(4, BufferUtils.getBytesCapacity(FloatBuffer.allocate(1)));
        assertEquals(4, BufferUtils.getBytesCapacity(ByteBuffer.allocate(4).asFloatBuffer()));
        assertEquals(4, BufferUtils.getBytesCapacity(ByteBuffer.allocateDirect(4).asFloatBuffer()));
    }
}
