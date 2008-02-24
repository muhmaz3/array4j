package net.lunglet.svm;

import com.googlecode.array4j.matrix.FloatVector;
import java.io.Serializable;

public final class SvmNode implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient Handle handle;

    private int index;

    public SvmNode(final int index) {
        this(index, (Handle) null);
    }

    public SvmNode(final int index, final FloatVector value) {
        this(index, new Handle() {
            @Override
            public FloatVector getData() {
                return value;
            }

            @Override
            public int getIndex() {
                throw new UnsupportedOperationException();
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

    public int getHandleIndex() {
        return handle.getIndex();
    }

    public int getIndex() {
        return index;
    }

    public FloatVector getValue() {
        if (handle == null) {
            throw new IllegalStateException();
        }
        return handle.getData();
    }

    public void setHandle(final Handle handle) {
        this.handle = handle;
    }
}
