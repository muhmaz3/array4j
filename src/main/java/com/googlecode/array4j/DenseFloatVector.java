package com.googlecode.array4j;

import java.util.Arrays;

public final class DenseFloatVector extends
        AbstractDenseVector<DenseFloatVector, DenseFloatSupport<DenseFloatVector, DenseFloatVector>, float[]> implements
        FloatVector<DenseFloatVector>, DenseVector<DenseFloatVector> {
    final float[] data;

    public DenseFloatVector() {
        this(0);
    }

    public DenseFloatVector(final float... values) {
        this(Orientation.ROW, values);
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

    public DenseFloatVector(final int size) {
        this(size, Orientation.DEFAULT_FOR_VECTOR);
    }

    public DenseFloatVector(final int size, final Orientation orientation) {
        this(new float[size], size, 0, 1, orientation);
    }

    public DenseFloatVector(final Orientation orientation, final float... values) {
        this(values, values.length, 0, 1, orientation);
    }

    void copyTo(final float[] target, final int targetOffset, final int targetStride) {
        for (int i = 0; i < size; i++) {
            target[targetOffset + i * targetStride] = data[offset + i * stride];
        }
    }

    public DenseFloatVector createColumnVector() {
        return new DenseFloatVector(rows, Orientation.COLUMN);
    }

    public DenseFloatVector createRowVector() {
        return new DenseFloatVector(rows, Orientation.ROW);
    }

    public DenseFloatVector createColumnVector(final float... values) {
        checkArgument(values.length == rows);
        return new DenseFloatVector(Orientation.COLUMN, values);
    }

    public DenseFloatVector createRowVector(final float... values) {
        checkArgument(values.length == columns);
        return new DenseFloatVector(Orientation.ROW, values);
    }

    public DenseFloatVector createVector(int size, int offset, int stride, Orientation orientation) {
        return new DenseFloatVector(data, size, offset, stride, orientation);
    }

    @Override
    public boolean equals(final Object obj) {
        // TODO possibly allow any FloatMatrix instance
        if (obj == null || !(obj instanceof DenseFloatVector)) {
            return false;
        }
        DenseFloatVector other = (DenseFloatVector) obj;
        // TODO orientation is ignored... do we want that?
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

    public float[] getData() {
        return data;
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

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    public DenseFloatVector transpose() {
        return new DenseFloatVector(data, size, offset, stride, orientation.transpose());
    }
}
