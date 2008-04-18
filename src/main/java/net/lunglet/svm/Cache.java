package net.lunglet.svm;

//
// Kernel Cache
//
// l is the number of total data items
// size is the cache size limit in bytes
//
final class Cache {
    private static final class Entry {
        float[] data;

        int len; // data[0,len) is cached in this entry

        Entry prev, next; // a cicular list
    }

    private final Entry[] entries;

    private final Entry lruHead;

    private long size;

    Cache(final int l, final long size) {
        this.size = size;
        entries = new Entry[l];
        for (int i = 0; i < l; i++) {
            entries[i] = new Entry();
        }
        this.size /= 4;
        // sizeof(head_t) == 16
        this.size -= l * (16L / 4L);
        // cache must be large enough for two columns
        this.size = Math.max(size, (long) 2 * l);
        lruHead = new Entry();
        lruHead.next = lruHead;
        lruHead.prev = lruHead;
    }

    // request data [0,len)
    // return some position p where [p,len) need to be filled
    // (p >= len if nothing needs to be filled)
    // java: simulate pointer using single-element array
    int getData(final int index, final float[][] data, int len) {
        Entry h = entries[index];
        if (h.len > 0) {
            lruDelete(h);
        }
        int more = len - h.len;

        if (more > 0) {
            // free old space
            while (size < more) {
                Entry old = lruHead.next;
                lruDelete(old);
                size += old.len;
                old.data = null;
                old.len = 0;
            }

            // allocate new space
            float[] newData = new float[len];
            if (h.data != null) {
                System.arraycopy(h.data, 0, newData, 0, h.len);
            }
            h.data = newData;
            size -= more;
            do {
                int other = h.len;
                h.len = len;
                len = other;
            } while (false);
        }

        lruInsert(h);
        data[0] = h.data;
        return len;
    }

    private void lruDelete(final Entry h) {
        // delete from current location
        h.prev.next = h.next;
        h.next.prev = h.prev;
    }

    private void lruInsert(final Entry h) {
        // insert to last position
        h.next = lruHead;
        h.prev = lruHead.prev;
        h.prev.next = h;
        h.next.prev = h;
    }

    void swapIndex(int i, int j) {
        if (i == j) {
            return;
        }

        if (entries[i].len > 0) {
            lruDelete(entries[i]);
        }
        if (entries[j].len > 0) {
            lruDelete(entries[j]);
        }
        do {
            float[] other = entries[i].data;
            entries[i].data = entries[j].data;
            entries[j].data = other;
        } while (false);
        do {
            int other = entries[i].len;
            entries[i].len = entries[j].len;
            entries[j].len = other;
        } while (false);
        if (entries[i].len > 0) {
            lruInsert(entries[i]);
        }
        if (entries[j].len > 0) {
            lruInsert(entries[j]);
        }

        if (i > j) {
            do {
                int other = i;
                i = j;
                j = other;
            } while (false);
        }
        for (Entry h = lruHead.next; h != lruHead; h = h.next) {
            if (h.len > i) {
                if (h.len > j) {
                    do {
                        float other = h.data[i];
                        h.data[i] = h.data[j];
                        h.data[j] = other;
                    } while (false);
                } else {
                    // give up
                    lruDelete(h);
                    size += h.len;
                    h.data = null;
                    h.len = 0;
                }
            }
        }
    }
}
