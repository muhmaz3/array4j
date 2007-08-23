package com.googlecode.array4j.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.Storage;

public final class BufferUtil {
    private BufferUtil() {
    }

    public static FloatBuffer createComplexFloatBuffer(final int size, final Storage storage) {
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(2 * size * Constants.FLOAT_BYTES);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        } else {
            return FloatBuffer.allocate(2 * size);
        }
    }

    public static FloatBuffer createFloatBuffer(final int size, final Storage storage) {
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size * Constants.FLOAT_BYTES);
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        } else {
            return FloatBuffer.allocate(size);
        }
    }
}
