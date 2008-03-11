package net.lunglet.array4j;

/**
 * Storage type.
 */
public enum Storage {
    DIRECT, HEAP;

    public static final Storage DEFAULT = HEAP;

    public Storage checkSame(final Storage... storages) {
        for (Storage storage : storages) {
            if (!equals(storage)) {
                throw new IllegalArgumentException("all matrices must have " + this + " storage");
            }
        }
        return this;
    }
}
