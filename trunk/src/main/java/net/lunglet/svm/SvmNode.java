package net.lunglet.svm;

import com.googlecode.array4j.FloatVector;

final class SvmNode {
    private final Handle handle;

    private final int index;

    public SvmNode(final int index) {
        this(index, (Handle) null);
    }

    public SvmNode(final int index, final FloatVector<?> value) {
        this(index, new Handle() {
            @Override
            public FloatVector<?> getData() {
                return value;
            }

            @Override
            public int getLabel() {
                throw new UnsupportedOperationException();
            }
        });
    }

    public SvmNode(final int index, final Handle handle) {
        this.index = index;
        this.handle = handle;
    }

    public int getIndex() {
        return index;
    }

    public FloatVector<?> getValue() {
        if (handle == null) {
            throw new IllegalStateException();
        }
        return handle.getData();
    }
}
