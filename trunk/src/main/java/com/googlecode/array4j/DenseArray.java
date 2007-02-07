package com.googlecode.array4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.array4j.kernel.Interface;

// TODO DoubleBuffer.asReadOnlyBuffer could be useful

public class DenseArray<E extends DenseArray> implements Array<E> {
    private final int[] fShape;

    private final DoubleBuffer fBuffer;

    private final Constructor<? extends DenseArray> bufferShapeConstructor;

    private final Constructor<? extends DenseArray> shapeConstructor;

    public DenseArray(final int... shape) {
        this(Interface.createDoubleBuffer(calculateSize(shape)), shape);
    }

    protected DenseArray(final double[] values) {
        this(values, new int[]{values.length});
    }

    public DenseArray(final Array other) {
        this(other.getShape());
        final DoubleBuffer buffer = other.getBuffer();
        buffer.rewind();
        fBuffer.put(buffer);
    }

    public DenseArray(final double[] values, final int... shape) {
        this(shape);
        fBuffer.put(values);
    }

    /** Constructor reshaping and other view-type operations. */
    public DenseArray(final DoubleBuffer buffer, final int[] shape) {
        checkShape(shape);
        if (calculateSize(shape) != buffer.capacity()) {
            throw new IllegalArgumentException("buffer size is invalid");
        }
        this.fShape = shape;
        this.fBuffer = buffer;
        try {
            this.bufferShapeConstructor = getClass().getConstructor(DoubleBuffer.class, int[].class);
            this.shapeConstructor = getClass().getConstructor(int[].class);
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static DenseArray valueOf(final double... values) {
        return new DenseArray(values);
    }

    private static int calculateSize(final int[] shape) {
        int capacity = 1;
        for (int dim : shape) {
            if (dim < 0) {
                throw new IllegalArgumentException();
            }
            capacity *= dim;
        }
        return capacity;
    }

    private static void checkShape(final int[] shape) {
        for (int dim : shape) {
            if (dim < 0) {
                throw new IllegalArgumentException("negative dimensions are not allowed");
            }
        }
    }

    public final int[] getShape() {
        final int[] shapeCopy = new int[fShape.length];
        System.arraycopy(fShape, 0, shapeCopy, 0, fShape.length);
        return shapeCopy;
    }

    public final int getShape(final int index) {
        return fShape[index];
    }

    public final int[] shape() {
        return getShape();
    }

    public final int shape(final int index) {
        return fShape[index];
    }

    public final int getSize() {
        return fBuffer.capacity();
    }

    public final int getNdim() {
        return fShape.length;
    }

    public final int ndim() {
        return fShape.length;
    }

    public final int size() {
        return getSize();
    }

    private int getCPosition(final int[] indexes) {
        int n = 0;
        int dimprod = 1;
        for (int i = indexes.length - 1; i >= 0; i--) {
            final int index = indexes[i];
            final int dim = fShape[i];
            if (index < 0 || index >= dim) {
                // TODO index < 0 can be valid
                throw new ArrayIndexOutOfBoundsException();
            }
            n += index * dimprod;
            // this dimension only affects the product for the next index
            dimprod *= dim;
        }
        return n;
    }

    public final double get(final int... indexes) {
        if (indexes.length != fShape.length) {
            throw new IllegalArgumentException();
        }
        return fBuffer.get(getCPosition(indexes));
    }

    public final void set(final double value, final int... indexes) {
        fBuffer.put(getCPosition(indexes), value);
    }

    /**
     * Fill array with scalar value.
     *
     * @param value
     *            value to fill with
     */
    public final void fill(final double value) {
        Interface.kernel().fill(value, fBuffer);
    }

    public final E reshape(final int... shape) {
        if (calculateSize(shape) != size()) {
            throw new IllegalArgumentException("total size of new array must be unchanged");
        }
        // return an array with the new shape, but with the same underlying buffer
        return callConstructor(bufferShapeConstructor, fBuffer, shape);
    }

    public final E log() {
        final E out = callConstructor(shapeConstructor, fShape);
        Interface.kernel().log(fBuffer, out.fBuffer);
        return out;
    }

    private E callConstructor(final Constructor<? extends DenseArray> constructor, final Object... args) {
        try {
            return (E) constructor.newInstance(args);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            final Throwable t = e.getTargetException();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            // This should never happen unless the constructors start throwing
            // checked exceptions, in which case this method must be changed to
            // also throw the same checked exceptions.
            throw new AssertionError();
        }
    }

    public final E logEquals() {
        Interface.kernel().log(fBuffer, fBuffer);
        return (E) this;
    }

    public final double sum() {
        return Interface.kernel().sum(fBuffer);
    }

    public final double sum(final int axis) {
        throw new UnsupportedOperationException();
    }

    public final E plusEquals(final double value) {
        Interface.kernel().plus(value, fBuffer, fBuffer);
        return (E) this;
    }

    public final E timesEquals(final double value) {
        Interface.kernel().times(value, fBuffer, fBuffer);
        return (E) this;
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof DenseArray)) {
          return false;
        }
        if (this == obj) {
          return true;
        }
        final DenseArray rhs = (DenseArray) obj;
        // TODO check shape and then values
        throw new UnsupportedOperationException();
    }

    public final DoubleBuffer getBuffer() {
        return fBuffer;
    }

    public static DenseArray arange(final double stop) {
        return arange(0.0, stop, 1.0);
    }

    public static DenseArray arange(final double start, final double stop) {
        return arange(start, stop, 1.0);
    }

    public static DenseArray arange(final double start, final double stop, final double step) {
        // TODO this is hopelessly inefficient
        final List<Double> range = new ArrayList<Double>();
        for (double x = start; x < stop; x += step) {
            range.add(x);
        }
        final double[] rangeArr = new double[range.size()];
        int i = 0;
        for (double x : range) {
            rangeArr[i++] = x;
        }
        return new DenseArray(rangeArr);
    }
}
