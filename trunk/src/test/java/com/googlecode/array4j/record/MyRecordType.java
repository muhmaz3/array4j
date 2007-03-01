package com.googlecode.array4j.record;

import com.googlecode.array4j.ByteOrder;
import com.googlecode.array4j.types.RecordType;

@Record(align = true)
public final class MyRecordType extends RecordType<MyRecordType> {
    @Integer
    public int int1;

    @Integer(byteOrder = ByteOrder.BIG_ENDIAN)
    public int int2;

    @Integer(byteOrder = ByteOrder.LITTLE_ENDIAN)
    public int int3;

    @Override
    public MyRecordType newByteOrder(final ByteOrder byteOrder) {
        return null;
    }
}
