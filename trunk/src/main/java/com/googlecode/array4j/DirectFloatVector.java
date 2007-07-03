package com.googlecode.array4j;

import java.nio.FloatBuffer;

public final class DirectFloatVector extends
        AbstractDenseVector<DirectFloatVector, DirectFloatSupport<DirectFloatVector, DirectFloatVector>, float[]>
        implements FloatVector<DirectFloatVector>, DenseVector<DirectFloatVector> {
    private final FloatBuffer data;

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

    public DirectFloatVector(final Orientation orientation, final float[] values) {
        this(values.length, 0, 1, orientation);
        getData().put(values);
    }

    public DirectFloatVector createColumnVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DirectFloatVector createRowVector() {
        // TODO Auto-generated method stub
        return null;
    }

    public DirectFloatVector createColumnVector(final float... values) {
        checkArgument(values.length == rows);
        return new DirectFloatVector(Orientation.COLUMN, values);
    }

    public DirectFloatVector createRowVector(final float... values) {
        checkArgument(values.length == columns);
        return new DirectFloatVector(Orientation.ROW, values);
    }

    public DirectFloatVector createVector(int size, int offset, int stride, Orientation orientation) {
        return new DirectFloatVector(getData(), size, offset, stride, orientation);
    }

    private FloatBuffer getData() {
        // TODO code duplicated from DirectFloatMatrix.getData
        return (FloatBuffer) ((FloatBuffer) data.rewind()).position(offset);
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
}
