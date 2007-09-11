package com.googlecode.array4j.util;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.Storage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public final class BufferUtils {
    public static FloatBuffer createComplexFloatBuffer(final int size, final Storage storage) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(2 * size * Constants.FLOAT_BYTES);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        } else {
            return FloatBuffer.allocate(2 * size);
        }
    }

    public static FloatBuffer createFloatBuffer(final int size, final Storage storage) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size * Constants.FLOAT_BYTES);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        } else {
            return FloatBuffer.allocate(size);
        }
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
