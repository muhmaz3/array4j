package net.lunglet.util;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.Storage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public final class BufferUtils {
    public static ByteBuffer createAlignedBuffer(final int size, final int alignment) {
        if (alignment < 1) {
            throw new IllegalArgumentException();
        }
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        // TODO get buffer address to calculate position for slice
        // TODO maybe use array4j_addressof here
//        ByteBuffer buffer = ByteBuffer.allocateDirect(size + alignment - 1);
        // TODO new Memory(size).align(alignment).getByteBuffer(0, size);
        return ((ByteBuffer) buffer.order(ByteOrder.nativeOrder()).position(0)).slice();
    }

    public static FloatBuffer createComplexFloatBuffer(final int size, final Storage storage) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = createAlignedBuffer(2 * size * Constants.FLOAT_BYTES, 16);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        }
        return FloatBuffer.allocate(2 * size);
    }

    public static DoubleBuffer createDoubleBuffer(final int size, final Storage storage) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = createAlignedBuffer(size * Constants.DOUBLE_BYTES, 16);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asDoubleBuffer();
        }
        return DoubleBuffer.allocate(size);
    }

    public static FloatBuffer createFloatBuffer(final int size, final Storage storage) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = createAlignedBuffer(size * Constants.FLOAT_BYTES, 16);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        }
        return FloatBuffer.allocate(size);
    }

    public static int getBytesCapacity(final Buffer buf) {
        if (buf instanceof ByteBuffer) {
            return buf.capacity();
        } else if (buf instanceof FloatBuffer) {
            return Constants.FLOAT_BYTES * buf.capacity();
        } else if (buf instanceof DoubleBuffer) {
            return Constants.DOUBLE_BYTES * buf.capacity();
        } else {
            // TODO add other buffers above
            throw new AssertionError();
        }
    }

    private BufferUtils() {
    }
}
