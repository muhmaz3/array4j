package net.lunglet.hdf;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * http://hdfgroup.com/hdf-java-html/JNI/index.html
 * http://hdfgroup.org/HDF5/doc/TechNotes/ThreadSafeLibrary.html
 */

final class NativeIdComponent extends WeakReference<IdComponent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NativeIdComponent.class);

    private static final int MAX_ITERATIONS = 100;

    private static final List<NativeIdComponent> REF_LIST =
        Collections.synchronizedList(new ArrayList<NativeIdComponent>());

    private static final ReferenceQueue<IdComponent> REF_QUEUE = new ReferenceQueue<IdComponent>();

    static void cleanup() {
        int iterations = 0;
        do {
            NativeIdComponent nativeId = null;
            nativeId = (NativeIdComponent) REF_QUEUE.poll();
            iterations++;
            if (nativeId != null) {
                nativeId.close();
            } else {
                break;
            }
        } while (iterations < MAX_ITERATIONS);
    }

    private final CloseAction closeAction;

    private final int id;

    private boolean open;

    public NativeIdComponent(final IdComponent component, final int id, final CloseAction closeAction) {
        super(component, REF_QUEUE);
        this.id = id;
        this.open = true;
        this.closeAction = closeAction;
        REF_LIST.add(this);
    }

    public void close() {
        // Ignore close if there is no close action. This is required for
        // components that cannot be closed, e.g. predefined types.
        if (open && closeAction != null) {
            LOGGER.debug("Closing [id={}]", getId());
            REF_LIST.remove(this);
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
