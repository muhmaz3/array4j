package com.googlecode.array4j;

import java.util.Arrays;

public final class DenseFloatVector extends AbstractDenseVector<DenseFloatVector> implements
        FloatVector<DenseFloatVector>, DenseVector<DenseFloatVector> {
    final float[] data;

    private final transient DenseFloatSupport<DenseFloatVector, DenseFloatVector> floatSupport;

    private final transient ToArraysConverter<float[]> arraysConverter;

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
        this.floatSupport = new DenseFloatSupport<DenseFloatVector, DenseFloatVector>(this, data);
        this.arraysConverter = new ToArraysConverter<float[]>() {
            @Override
            protected float[] createArray(final int length) {
                return new float[length];
            }

            @Override
            protected float[][] createArrayArray(final int length) {
                return new float[length][];
            }

            @Override
            protected void set(final int srcPos, final float[] dest, final int destPos) {
                dest[destPos] = data[srcPos];
            }
        };
    }

    public DenseFloatVector(final int size) {
        this(size, Orientation.ROW);
    }

    public DenseFloatVector(final int size, final Orientation orientation) {
        this(new float[size], size, 0, 1, orientation);
    }

    public DenseFloatVector(final Orientation orientation, final float... values) {
        this(values, values.length, 0, 1, orientation);
    }

    public DenseFloatVector column(final int column) {
        return matrixSupport.column(column);
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

    public DenseFloatVector createSharingVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
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

    public DenseFloatVector row(final int row) {
        return matrixSupport.row(row);
    }

    public void setColumn(final int column, final FloatVector columnVector) {
        floatSupport.setColumn(column, columnVector);
    }

    public void setRow(final int row, final FloatVector rowVector) {
        floatSupport.setRow(row, rowVector);
    }

    public float[] toArray() {
        return floatSupport.toArray();
    }

    public float[][] toColumnArrays() {
        return arraysConverter.toArrays(columns, rows, false);
    }

    public float[][] toRowArrays() {
        return arraysConverter.toArrays(rows, columns, true);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    public DenseFloatVector transpose() {
        return new DenseFloatVector(data, size, offset, stride, orientation.transpose());
    }
}
