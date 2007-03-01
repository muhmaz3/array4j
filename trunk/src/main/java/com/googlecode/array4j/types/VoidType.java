package com.googlecode.array4j.types;

import com.googlecode.array4j.ByteOrder;

public final class VoidType extends FlexibleType<VoidType> {
    @Override
    public VoidType newByteOrder(final ByteOrder byteOrder) {
        return null;
    }
}
