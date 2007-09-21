package net.lunglet.hdf;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/*
 * http://hdfgroup.com/hdf-java-html/JNI/index.html
 * http://hdfgroup.org/HDF5/doc/TechNotes/ThreadSafeLibrary.html
 */

final class NativeIdComponent extends WeakReference<IdComponent> {
    private static final int MAX_ITERATIONS = 100;

    private static ThreadLocal<List<NativeIdComponent>> refList = new ThreadLocal<List<NativeIdComponent>>() {
        @Override
        protected List<NativeIdComponent> initialValue() {
            return new ArrayList<NativeIdComponent>();
        }
    };

    private static ThreadLocal<ReferenceQueue<IdComponent>> refQueue = new ThreadLocal<ReferenceQueue<IdComponent>>() {
        @Override
        protected ReferenceQueue<IdComponent> initialValue() {
            return new ReferenceQueue<IdComponent>();
        }
    };

    static void cleanup() {
        ReferenceQueue<IdComponent> r = refQueue.get();
        NativeIdComponent nativeId = (NativeIdComponent) r.poll();
        int iterations = 0;
        while (nativeId != null && iterations < MAX_ITERATIONS) {
            if (nativeId.open) {
                nativeId.close();
            }
            iterations++;
            nativeId = (NativeIdComponent) r.poll();
        }
    }

    private final CloseAction closeAction;

    private final int id;

    private boolean open;

    public NativeIdComponent(final IdComponent component, final int id, final CloseAction closeAction) {
        super(component, refQueue.get());
        this.id = id;
        this.open = true;
        this.closeAction = closeAction;
        refList.get().add(this);
    }

    public void close() {
        // Ignore close if there is no close action. This is required for
        // components that cannot be closed, e.g. predefined types.
        if (closeAction != null) {
            refList.get().remove(this);
            closeAction.close(getId());
            open = false;
        }
    }

    public int getId() {
        if (!open) {
            throw new IllegalStateException();
        }
        return id;
    }

    public boolean isOpen() {
        return open;
    }
}
