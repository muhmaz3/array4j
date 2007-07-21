package com.googlecode.array4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.FloatBuffer;

public final class DirectFloatVector extends
        AbstractDenseVector<DirectFloatVector, DirectFloatSupport<DirectFloatVector, DirectFloatVector>, float[]>
        implements FloatVector<DirectFloatVector>, DenseVector<DirectFloatVector> {
    private final FloatBuffer data;

    public DirectFloatVector(final float... values) {
        this(Orientation.DEFAULT_FOR_VECTOR, values);
    }

    DirectFloatVector(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(size, offset, stride, orientation);
        checkArgument(data != null);
        this.data = data;
        this.support = new DirectFloatSupport<DirectFloatVector, DirectFloatVector>(this, data);
    }

    public DirectFloatVector(final int size) {
        this(size, 0, 1, Orientation.DEFAULT_FOR_VECTOR);
    }

    public DirectFloatVector(final int size, final int offset, final int stride, final Orientation orientation) {
        this(DirectFloatMatrix.createBuffer(size), size, offset, stride, orientation);
    }

    public DirectFloatVector(final int size, final Orientation orientation) {
        this(size, 0, 1, orientation);
    }

    public DirectFloatVector(final Orientation orientation, final float[] values) {
        this(values.length, 0, 1, orientation);
        data().put(values);
    }

    @Override
    public DirectFloatVector asVector() {
        throw new UnsupportedOperationException();
    }

    void copyTo(final FloatBuffer target, final int targetOffset, final int targetStride) {
        for (int i = 0; i < size; i++) {
            target.put(targetOffset + i * targetStride, data().get(i * stride));
        }
    }

    @Override
    public DirectFloatVector createColumnVector() {
        return new DirectFloatVector(rows, Orientation.COLUMN);
    }

    @Override
    public DirectFloatVector createColumnVector(final float... values) {
        checkArgument(values.length == rows);
        return new DirectFloatVector(Orientation.COLUMN, values);
    }

    @Override
    public DirectFloatVector createRowVector() {
        return new DirectFloatVector(columns, Orientation.ROW);
    }

    @Override
    public DirectFloatVector createRowVector(final float... values) {
        checkArgument(values.length == columns);
        return new DirectFloatVector(Orientation.ROW, values);
    }

    @Override
    public DirectFloatVector createVector(final int size) {
        return new DirectFloatVector(size);
    }

    public DirectFloatVector createVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DirectFloatVector(data(), size, offset, stride, orientation);
    }

    public FloatBuffer data() {
        return (FloatBuffer) data.rewind();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof DirectFloatVector)) {
            return false;
        }
        final DirectFloatVector other = (DirectFloatVector) obj;
        if (size != other.size || !orientation.equals(other.orientation)) {
            return false;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void fill(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float get(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DirectFloatVector minus(final FloatVector<?> other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void plusEquals(final FloatVector<?> other) {
        throw new UnsupportedOperationException();
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.support = new DirectFloatSupport<DirectFloatVector, DirectFloatVector>(this, data);
    }

    @Override
    public void set(final int index, final float value) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public float[] toArray() {
        return support.toArray();
    }

    @Override
    public DirectFloatVector transpose() {
        return new DirectFloatVector(data(), size, offset, stride, orientation.transpose());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
