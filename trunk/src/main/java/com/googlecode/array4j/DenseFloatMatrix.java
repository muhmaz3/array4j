package com.googlecode.array4j;

import com.googlecode.array4j.internal.ToArraysConverter;

public final class DenseFloatMatrix
        extends
        AbstractDenseMatrix<DenseFloatMatrix, DenseFloatVector, DenseFloatSupport<DenseFloatMatrix, DenseFloatVector>, float[]>
        implements FloatMatrix<DenseFloatMatrix, DenseFloatVector>, DenseMatrix<DenseFloatMatrix, DenseFloatVector> {
    private final float[] data;

    public DenseFloatMatrix(final float[] data, final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(rows, columns, offset, stride, orientation);
        checkArgument(data != null);
        checkArgument(size == 0 || offset < data.length);
        checkArgument(size <= 1 || data.length >= stride * size);
        this.data = data;
        this.support = new DenseFloatSupport<DenseFloatMatrix, DenseFloatVector>(this, data);
    }

    public DenseFloatMatrix(final float[] values, final int rows, final int columns, final Orientation orientation) {
        this(values, rows, columns, 0, 1, orientation);
    }

    public DenseFloatMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.DEFAULT);
    }

    public DenseFloatMatrix(final int rows, final int columns, final Orientation orientation) {
        this(new float[rows * columns], rows, columns, 0, 1, orientation);
    }

    @Override
    protected ToArraysConverter<DenseFloatMatrix, float[]> createArraysConverter() {
        return new ToArraysConverter<DenseFloatMatrix, float[]>(this) {
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

    public DenseFloatVector createColumnVector() {
        return new DenseFloatVector(rows, Orientation.COLUMN);
    }

    public DenseFloatVector createRowVector() {
        return new DenseFloatVector(columns, Orientation.ROW);
    }

    public DenseFloatVector createVector(int size, int offset, int stride, Orientation orientation) {
        return new DenseFloatVector(data, size, offset, stride, orientation);
    }

    @Override
    public boolean equals(final Object obj) {
        // TODO possibly allow any FloatMatrix instance
        if (obj == null || !(obj instanceof DenseFloatMatrix)) {
            return false;
        }
        DenseFloatMatrix other = (DenseFloatMatrix) obj;
        // TODO orientation is ignored... do we want that?
        if (rows != other.rows || columns != other.columns) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (data[offset + i * stride] != other.data[other.offset + i * other.stride]) {
                return false;
            }
        }
        return true;
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
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%d x %d\n", rows, columns));
        for (DenseFloatVector row : rowsIterator()) {
            builder.append(row);
            builder.append("\n");
        }
        return builder.toString();
    }

    public DenseFloatMatrix transpose() {
        // interchange columns and rows and change orientation
        return new DenseFloatMatrix(data, columns, rows, offset, stride, orientation.transpose());
    }

    // TODO implement almostEquals that allows an epsilon
}
