package com.googlecode.array4j.record;

import java.nio.ByteBuffer;

import com.googlecode.array4j.kernel.KernelType;

public final class MyRecordArray extends AbstractRecordArray<MyRecordArray> {
    private MyRecordArray() {
        super(MyRecordType.class);
    }

    @Override
    protected MyRecordArray create(int[] dims, int[] strides, ByteBuffer data, int flags, KernelType kernelType) {
        // TODO get rid of this stuff and use reflection to call constructor instead
        return null;
    }
}
