package com.googlecode.array4j.record;

import java.nio.ByteBuffer;

import com.googlecode.array4j.AbstractArray;
import com.googlecode.array4j.types.RecordType;

public abstract class AbstractRecordArray<E extends AbstractRecordArray> extends AbstractArray<E> {
    private ByteBuffer fData;

    protected AbstractRecordArray(final Class<? extends RecordType<?>> dtypeClass) {
        super(null);
    }

    public final int elementSize() {
        return 0;
    }

    @Override
    protected final void setBuffer(final ByteBuffer data) {
        fData = (ByteBuffer) data.duplicate().rewind();
    }
}
