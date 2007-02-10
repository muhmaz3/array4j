package com.googlecode.array4j;

public final class Indexing {
    private static final Slice SLICE = new Slice();

    private static final Ellipsis ELLIPSIS = new Ellipsis();

    private static final NewAxis NEWAXIS = new NewAxis();

    private Indexing() {
    }

    public interface Index {
    }

    private static final class Slice implements Index {
        public final int start;

        public final int stop;

        public final int step;

        public Slice() {
            this(0, Integer.MAX_VALUE);
        }

        public Slice(final int stop) {
            this(0, stop);
        }

        public Slice(final int start, final int stop) {
            this(start, stop, 1);
        }

        public Slice(final int start, final int stop, final int step) {
            this.start = start;
            this.stop = stop;
            this.step = step;
        }
    }

    private static final class Ellipsis implements Index {
    }

    private static final class NewAxis implements Index {
    }

    public static Slice slice() {
        return SLICE;
    }

    public static Slice slice(final int stop) {
        return new Slice(stop);
    }

    public static Slice slice(final int start, final int stop) {
        return new Slice(start, stop);
    }

    public static Slice slice(final int start, final int stop, final int step) {
        return new Slice(start, stop, step);
    }

    public static Ellipsis ellipsis() {
        return ELLIPSIS;
    }

    public static NewAxis newaxis() {
        return NEWAXIS;
    }
}
