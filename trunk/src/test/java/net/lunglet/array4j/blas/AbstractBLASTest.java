package net.lunglet.array4j.blas;

import java.util.Arrays;
import java.util.Iterator;

public abstract class AbstractBLASTest {
    protected static final class Permutations<E> implements Iterable<E[]> {
        private boolean done;

        private final int[] idx;

        private final int length;

        private final E[] values;

        public Permutations(final int length, final E... values) {
            if (length < 1) {
                throw new IllegalArgumentException();
            }
            this.length = length;
            this.values = Arrays.copyOf(values, values.length);
            this.idx = new int[length];
            this.done = false;
        }

        private void increment(final int i) {
            if (idx[i] < values.length - 1) {
                idx[i]++;
            } else {
                idx[i] = 0;
                if (i < idx.length - 1) {
                    increment(i + 1);
                } else {
                    done = true;
                }
            }
        }

        @Override
        public Iterator<E[]> iterator() {
            return new Iterator<E[]>() {
                @Override
                public boolean hasNext() {
                    return !done;
                }

                @SuppressWarnings("unchecked")
                @Override
                public E[] next() {
                    E[] perm = (E[]) Arrays.copyOf(values, length, values.getClass());
                    for (int i = 0; i < perm.length; i++) {
                        perm[i] = values[idx[i]];
                    }
                    increment(0);
                    return perm;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}
