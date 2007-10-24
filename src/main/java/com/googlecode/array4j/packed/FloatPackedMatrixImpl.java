package com.googlecode.array4j.packed;

import com.googlecode.array4j.Direction;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.DenseFactory;
import com.googlecode.array4j.dense.FloatDenseVector;
import com.googlecode.array4j.math.FloatMatrixUtils;
import com.googlecode.array4j.util.BufferUtils;
import java.nio.FloatBuffer;

final class FloatPackedMatrixImpl extends AbstractPackedMatrix<FloatDenseVector> implements FloatPackedMatrix {
    private static final long serialVersionUID = 1L;

    private transient FloatBuffer data;

    FloatPackedMatrixImpl(final FloatBuffer data, final int rows, final int columns, final PackedType packedType) {
        super(rows, columns, packedType);
        this.data = data;
    }

    FloatPackedMatrixImpl(final int rows, final int columns, final PackedType packedType, final Storage storage) {
        super(rows, columns, packedType);
        this.data = BufferUtils.createFloatBuffer(getBufferSize(), storage);
    }

    @Override
    public FloatDenseVector column(final int column) {
        FloatDenseVector v = DenseFactory.createFloatVector(rows(), Direction.COLUMN, storage());
        for (int row = 0; row < rows; row++) {
            v.set(row, get(row, column));
        }
        return v;
    }

    public FloatBuffer data() {
        return ((FloatBuffer) data.position(0)).slice();
    }

    @Override
    public void divideEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float get(final int row, final int column) {
        if (nonZeroElement(row, column)) {
            return data.get(elementOffset(row, column));
        } else {
            return 0.0f;
        }
    }

    @Override
    public void minusEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void plusEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatDenseVector row(final int row) {
        FloatDenseVector v = DenseFactory.createFloatVector(columns(), Direction.ROW, storage());
        for (int column = 0; column < columns; column++) {
            v.set(column, get(row, column));
        }
        return v;
    }

    @Override
    public void set(final int row, final int column, final float value) {
        checkCanSet(row, column);
        data.put(elementOffset(row, column), value);
    }

    @Override
    public void setColumn(final int column, final FloatVector columnVector) {
        checkColumnIndex(column);
        checkColumnVector(columnVector);
        for (int row = 0; row < rows; row++) {
            if (nonZeroElement(row, column)) {
                set(row, column, columnVector.get(row));
            } else {
                if (columnVector.get(row) != 0.0f) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    @Override
    public void setRow(final int row, final FloatVector rowVector) {
        checkRowIndex(row);
        checkRowVector(rowVector);
        for (int column = 0; column < columns; column++) {
            if (nonZeroElement(row, column)) {
                set(row, column, rowVector.get(column));
            } else {
                if (rowVector.get(column) != 0.0f) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    @Override
    public Storage storage() {
        return data.isDirect() ? Storage.DIRECT : Storage.HEAP;
    }

    @Override
    public void timesEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float[] toArray() {
        // TODO figure out if this returns a dense array or a packed array
        // TODO a packed array will probably be more useful
        throw new UnsupportedOperationException();
    }

    @Override
    public float[][] toColumnArrays() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float[][] toRowArrays() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return FloatMatrixUtils.toString(this);
    }

    @Override
    public FloatPackedMatrix transpose() {
        return new FloatPackedMatrixImpl(data, rows, columns, packedType.transpose());
    }

    @Override
    public void plusEquals(final FloatMatrix other) {
        if (!(other instanceof FloatPackedMatrix)) {
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException();
    }
}
