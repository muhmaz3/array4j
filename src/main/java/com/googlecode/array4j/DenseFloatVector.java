package com.googlecode.array4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import com.googlecode.array4j.blas.DenseFloatBLAS;

public final class DenseFloatVector extends
        AbstractDenseVector<DenseFloatVector, DenseFloatSupport<DenseFloatVector, DenseFloatVector>, float[]> implements
        FloatVector<DenseFloatVector>, DenseVector<DenseFloatVector> {
    private final float[] data;

    public DenseFloatVector(final float... values) {
        this(Orientation.DEFAULT_FOR_VECTOR, values);
    }

    DenseFloatVector(final float[] data, final int size, final int stride) {
        this(data, size, 0, stride, Orientation.DEFAULT_FOR_VECTOR);
    }

    public DenseFloatVector(final float[] data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(size, offset, stride, orientation);
        checkArgument(data != null);
        checkArgument(size == 0 || offset < data.length);
        checkArgument(data.length >= size * stride);
        this.data = data;
        this.support = new DenseFloatSupport<DenseFloatVector, DenseFloatVector>(this, data);
    }

    /**
     * Copy constructor.
     *
     * @param other
     *                instance to copy from
     */
    public DenseFloatVector(final FloatVector<?> other) {
        super(other.size(), 0, 1, other.orientation());
        this.data = new float[size];
        this.support = new DenseFloatSupport<DenseFloatVector, DenseFloatVector>(this, data);
        for (int i = 0; i < size; i++) {
            data[i] = other.get(i);
        }
    }

    public DenseFloatVector(final int size) {
        this(size, Orientation.DEFAULT_FOR_VECTOR);
    }

    public DenseFloatVector(final int size, final Orientation orientation) {
        this(new float[checkArgumentNonNegative(size)], size, 0, 1, orientation);
    }

    public DenseFloatVector(final Orientation orientation, final float... values) {
        this(values, values.length, 0, 1, orientation);
    }

    @Override
    public DenseFloatVector asVector() {
        return this;
    }

    void copyTo(final float[] target, final int targetOffset, final int targetStride) {
        for (int i = 0; i < size; i++) {
            target[targetOffset + i * targetStride] = data[offset + i * stride];
        }
    }

    public DenseFloatVector createColumnVector() {
        return new DenseFloatVector(rows, Orientation.COLUMN);
    }

    public DenseFloatVector createColumnVector(final float... values) {
        checkArgument(values.length == rows);
        return new DenseFloatVector(Orientation.COLUMN, values);
    }

    public DenseFloatVector createRowVector() {
        return new DenseFloatVector(rows, Orientation.ROW);
    }

    public DenseFloatVector createRowVector(final float... values) {
        checkArgument(values.length == columns);
        return new DenseFloatVector(Orientation.ROW, values);
    }

    @Override
    public DenseFloatVector createVector(final int size) {
        return new DenseFloatVector(size);
    }

    public DenseFloatVector createVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DenseFloatVector(data, size, offset, stride, orientation);
    }

    public float[] data() {
        return data;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof DenseFloatVector)) {
            return false;
        }
        final DenseFloatVector other = (DenseFloatVector) obj;
        if (size != other.size || !orientation.equals(other.orientation)) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (data[offset + i * stride] != other.data[other.offset + i * other.stride]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void fill(final float value) {
        DenseFloatBLAS.INSTANCE.copy(new DenseFloatVector(new float[]{value}, size, 0), this);
    }

    @Override
    public float get(final int index) {
        return support.getValue(index);
    }

    @Override
    public DenseFloatVector minus(final FloatVector<?> other) {
        // TODO check size of other
        if (!(other instanceof DenseFloatVector)) {
            throw new UnsupportedOperationException();
        }
        DenseFloatVector x = (DenseFloatVector) other;
        DenseFloatVector y = new DenseFloatVector(this);
        DenseFloatBLAS.INSTANCE.axpy(-1.0f, x, y);
        return y;
    }

    @Override
    public void plusEquals(final FloatVector<?> other) {
        if (!(other instanceof DenseFloatVector)) {
            throw new UnsupportedOperationException();
        }
        DenseFloatVector x = (DenseFloatVector) other;
        DenseFloatBLAS.INSTANCE.axpy(1.0f, x, this);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.support = new DenseFloatSupport<DenseFloatVector, DenseFloatVector>(this, data);
    }

    @Override
    public void set(final int index, final float value) {
        support.setValue(index, value);
    }

    @Override
    public void setColumn(final int column, final FloatVector<?> columnVector) {
         support.setColumn(column, columnVector);
    }

    @Override
    public void setRow(final int row, final FloatVector<?> rowVector) {
         support.setRow(row, rowVector);
    }

    @Override
    public void timesEquals(final float value) {
        DenseFloatBLAS.INSTANCE.scal(value, this);
    }

    public float[] toArray() {
        return support.toArray();
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    public DenseFloatVector transpose() {
        return new DenseFloatVector(data, size, offset, stride, orientation.transpose());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
