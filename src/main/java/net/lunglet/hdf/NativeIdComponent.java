package net.lunglet.hdf;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO close shouldn't have to be synchronized

final class NativeIdComponent extends WeakReference<IdComponent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NativeIdComponent.class);

    private static final int MAX_ITERATIONS = 100;

    private static final List<NativeIdComponent> REF_LIST =
            Collections.synchronizedList(new ArrayList<NativeIdComponent>());

    private static final ReferenceQueue<IdComponent> REF_QUEUE = new ReferenceQueue<IdComponent>();

    static void cleanup() {
        int iterations = 0;
        do {
            // ReferenceQueue synchronizes poll internally, so different threads
            // should not receive the same object here
            NativeIdComponent nativeId = (NativeIdComponent) REF_QUEUE.poll();
            if (nativeId == null) {
                break;
            }
            nativeId.close();
            iterations++;
        } while (iterations < MAX_ITERATIONS);
    }

    private final CloseAction closeAction;

    private final int id;

    private boolean open;

    private Throwable closeStack = null;

    public NativeIdComponent(final IdComponent component, final int id, final CloseAction closeAction) {
        super(component, REF_QUEUE);
        this.id = id;
        this.open = true;
        this.closeAction = closeAction;
        REF_LIST.add(this);
    }

    public synchronized void close() {
        // Ignore close if there is no close action. This is required for
        // components that cannot be closed, e.g. predefined types.
        if (open && closeAction != null) {
            LOGGER.debug("Closing [id={}]", getId());
            synchronized (H5Library.INSTANCE) {
                closeAction.close(getId());
            }
            REF_LIST.remove(this);
            if (false) {
                closeStack = new Throwable().fillInStackTrace();
            }
            open = false;
        }
    }

    public int getId() {
        if (!open) {
            throw new IllegalStateException("Already closed", closeStack);
        }
        return id;
    }

    public boolean isOpen() {
        return open;
    }
}
