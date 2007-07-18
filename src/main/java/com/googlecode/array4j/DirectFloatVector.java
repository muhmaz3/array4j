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
        this.data = data;
        this.support = new DirectFloatSupport<DirectFloatVector, DirectFloatVector>(this, data);
        checkPostcondition(getData().remaining() >= size);
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
        getData().put(values);
    }

    void copyTo(final FloatBuffer target, final int targetOffset, final int targetStride) {
        for (int i = 0; i < size; i++) {
            target.put(targetOffset + i * targetStride, getData().get(i * stride));
        }
    }

    public DirectFloatVector createColumnVector() {
        return new DirectFloatVector(rows, Orientation.COLUMN);
    }

    public DirectFloatVector createColumnVector(final float... values) {
        checkArgument(values.length == rows);
        return new DirectFloatVector(Orientation.COLUMN, values);
    }

    public DirectFloatVector createRowVector() {
        return new DirectFloatVector(columns, Orientation.ROW);
    }

    public DirectFloatVector createRowVector(final float... values) {
        checkArgument(values.length == columns);
        return new DirectFloatVector(Orientation.ROW, values);
    }

    public DirectFloatVector createVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DirectFloatVector(getData(), size, offset, stride, orientation);
    }

    public FloatBuffer getData() {
        return (FloatBuffer) data.rewind();
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.support = new DirectFloatSupport<DirectFloatVector, DirectFloatVector>(this, data);
    }

    public void setColumn(final int column, final FloatVector<?> columnVector) {
        support.setColumn(column, columnVector);
    }

    public void setRow(final int row, final FloatVector<?> rowVector) {
        support.setRow(row, rowVector);
    }

    public float[] toArray() {
        return support.toArray();
    }

    public DirectFloatVector transpose() {
        return new DirectFloatVector(getData(), size, offset, stride, orientation.transpose());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
