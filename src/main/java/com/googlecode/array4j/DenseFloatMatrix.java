package com.googlecode.array4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.googlecode.array4j.blas.DenseFloatBLAS;

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

    /**
     * Copy constructor.
     *
     * @param other
     *                instance to copy from
     */
    public DenseFloatMatrix(final FloatMatrix<?, ?> other) {
        this(other.rows(), other.columns(), Orientation.DEFAULT);
        // TODO lots of optimization possible here
        for (int i = 0; i < rows; i++) {
            setRow(i, other.row(i));
        }
    }

    public DenseFloatMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.DEFAULT);
    }

    public DenseFloatMatrix(final int rows, final int columns, final Orientation orientation) {
        this(new float[rows * columns], rows, columns, 0, 1, orientation);
    }

    @Override
    public DenseFloatVector asVector() {
        return new DenseFloatVector(data, size, offset, stride, Orientation.DEFAULT_FOR_VECTOR);
    }

    public DenseFloatVector createColumnVector() {
        return new DenseFloatVector(rows, Orientation.COLUMN);
    }

    public DenseFloatVector createColumnVector(final float... values) {
        checkArgument(values.length == rows);
        return new DenseFloatVector(Orientation.COLUMN, values);
    }

    public DenseFloatVector createRowVector() {
        return new DenseFloatVector(columns, Orientation.ROW);
    }

    public DenseFloatVector createRowVector(final float... values) {
        checkArgument(values.length == columns);
        return new DenseFloatVector(Orientation.ROW, values);
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
        if (obj == null || !(obj instanceof DenseFloatMatrix)) {
            return false;
        }
        final DenseFloatMatrix other = (DenseFloatMatrix) obj;
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

    @Override
    public void fill(final float value) {
        DenseFloatBLAS.INSTANCE.copy(new DenseFloatVector(new float[]{value}, size, 0), asVector());
    }

    @Override
    public float get(final int row, final int column) {
        return support.getValue(row, column);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.support = new DenseFloatSupport<DenseFloatMatrix, DenseFloatVector>(this, data);
    }

    @Override
    public void set(final int row, final int column, final float value) {
        support.setValue(row, column, value);
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
    public DenseFloatMatrix subMatrixColumns(final int column0, final int column1) {
        checkArgument(column0 >= 0 && column0 < columns());
        checkArgument(column1 >= column0 && column1 < columns());
        int outputColumns = column1 - column0 + 1;
        DenseFloatMatrix output = new DenseFloatMatrix(rows, outputColumns);
        // TODO optimize this so that it only copies when necessary
        for (int i = 0, column = column0; column <= column1; i++, column++) {
            output.setColumn(i, column(column));
        }
        return output;
    }

    @Override
    public float[] toArray() {
        return support.toArray();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%d x %d\n", rows, columns));
        for (final DenseFloatVector row : rowsIterator()) {
            builder.append(row);
            // TODO don't append newline after last row
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public DenseFloatMatrix transpose() {
        // interchange columns and rows and change orientation
        return new DenseFloatMatrix(data, columns, rows, offset, stride, orientation.transpose());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    // TODO implement almostEquals that allows an epsilon
    // TODO almostEquals should work on any FloatMatrix<?, ?>
}
