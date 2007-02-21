package com.googlecode.array4j;

public final class Indexing {
    private static final Slice SLICE = new Slice();

    private static final Ellipsis ELLIPSIS = new Ellipsis();

    private static final NewAxis NEWAXIS = new NewAxis();

    private Indexing() {
    }

    public interface Index {
    }

    public static final class Ellipsis implements Index {
    }

    public static final class NewAxis implements Index {
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

    public static Slice sliceStart(final int start) {
        return new Slice(start, null, null);
    }

    public static Slice sliceStep(final int step) {
        return new Slice(null, null, step);
    }

    public static Ellipsis ellipsis() {
        return ELLIPSIS;
    }

    public static NewAxis newaxis() {
        return NEWAXIS;
    }
}
