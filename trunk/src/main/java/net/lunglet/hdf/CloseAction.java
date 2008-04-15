package net.lunglet.hdf;

/**
 * Action to close an HDF resource.
 * <p>
 * Close actions need not synchronize calls to the HDF library, since this will
 * be handled by NativeIdComponent.
 */
interface CloseAction {
    void close(final int id);
}
