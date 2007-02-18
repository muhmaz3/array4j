package com.googlecode.array4j;

import com.googlecode.array4j.Indexing.Index;

/**
 * Slice.
 * <p>
 * Some methods are based on code from the <CODE>slice_GetIndices</CODE>
 * function in NumPy, which is basically the same as <CODE>PySlice_GetIndicesEx</CODE>
 * in Python 2.3 and later.
 *
 * @author albert
 */
public final class Slice implements Index {
    private final Integer fStart;

    private final Integer fStop;

    private final Integer fStep;

    public Slice() {
        this(0, Integer.MAX_VALUE);
    }

    public Slice(final Integer stop) {
        this(null, stop);
    }

    public Slice(final Integer start, final Integer stop) {
        this(start, stop, null);
    }

    public Slice(final Integer start, final Integer stop, final Integer step) {
        if (step != null && step == 0) {
            throw new IllegalArgumentException("slice step cannot be zero");
        }
        this.fStart = start;
        this.fStop = stop;
        this.fStep = step;
    }

    public int getStart(final int maxLength) {
        final int step = getStep();
        if (fStart == null) {
            return step < 0 ? maxLength - 1 : 0;
        }
        int start = fStart;
        if (start < 0) {
            start += maxLength;
        }
        if (start < 0) {
            start = step < 0 ? -1 : 0;
        }
        if (start >= maxLength) {
            start = step < 0 ? maxLength - 1 : maxLength;
        }
        return start;
    }

    public int getStop(final int maxLength) {
        if (fStop == null) {
            return getStep() < 0 ? -1 : maxLength;
        }
        int stop = fStop;
        if (stop < 0) {
            stop += maxLength;
        }
        if (stop < 0) {
            stop = -1;
        }
        if (stop > maxLength) {
            stop = maxLength;
        }
        return stop;
    }

    public int getStep() {
        if (fStep == null) {
            return 1;
        }
        return fStep;
    }

    public int getSliceLength(final int maxLength) {
        final int start = getStart(maxLength);
        final int stop = getStop(maxLength);
        final int step = getStep();
        if ((step < 0 && stop >= start) || (step > 0 && start >= stop)) {
            return 0;
        } else if (step < 0) {
            return (stop - start + 1) / (step + 1);
        } else {
            return (stop - start - 1) / (step + 1);
        }
    }
}
