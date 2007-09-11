package com.googlecode.array4j.packed;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatMatrixUtils;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseMatrix;
import com.googlecode.array4j.dense.FloatDenseVector;
import com.googlecode.array4j.util.BufferUtils;
import java.nio.FloatBuffer;

public final class FloatPackedMatrix extends AbstractPackedMatrix<FloatPackedMatrix, FloatDenseVector> implements
        FloatMatrix<FloatPackedMatrix, FloatDenseVector> {
    private static final long serialVersionUID = 1L;

    public static FloatPackedMatrix createLowerTriangular(final int rows, final int columns) {
        return new FloatPackedMatrix(rows, columns, PackedType.LOWER_TRIANGULAR, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatPackedMatrix createSymmetric(final int dim) {
        return new FloatPackedMatrix(dim, dim, PackedType.SYMMETRIC, Storage.DEFAULT_FOR_DENSE);
    }

    public static FloatPackedMatrix valueOf(final FloatDenseMatrix other) {
        if (other.rows() != other.columns()) {
            throw new IllegalArgumentException();
        }
        int dim = other.rows();
        FloatPackedMatrix symm = new FloatPackedMatrix(dim, dim, PackedType.SYMMETRIC, other.storage());
        for (int i = 0; i < symm.columns(); i++) {
            symm.setColumn(i, other.column(i));
        }
        return symm;
    }

    public static FloatPackedMatrix createSymmetric(final int dim, final Storage storage) {
        return new FloatPackedMatrix(dim, dim, PackedType.SYMMETRIC, storage);
    }

    public static FloatPackedMatrix createUpperTriangular(final int rows, final int columns) {
        return new FloatPackedMatrix(rows, columns, PackedType.UPPER_TRIANGULAR, Storage.DEFAULT_FOR_DENSE);
    }

    private transient FloatBuffer data;

    private FloatPackedMatrix(final FloatBuffer data, final int rows, final int columns, final PackedType packedType) {
        super(rows, columns, packedType);
        this.data = data;
    }


    private FloatPackedMatrix(final int rows, final int columns, final PackedType packedType, final Storage storage) {
        super(rows, columns, packedType);
        this.data = BufferUtils.createFloatBuffer(getBufferSize(), storage);
    }

    @Override
    public FloatDenseVector asVector() {
        // TODO need to make a dense copy here and return that as a vector
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatDenseVector column(final int column) {
        checkColumnIndex(column);
        FloatDenseVector v = createColumnVector();
        for (int row = 0; row < rows; row++) {
            v.set(row, get(row, column));
        }
        return v;
    }

    @Override
    public FloatDenseVector createColumnVector() {
        return new FloatDenseVector(rows(), Orientation.COLUMN, storage());
    }

    @Override
    public FloatDenseVector createRowVector() {
        return new FloatDenseVector(columns(), Orientation.ROW, storage());
    }

    public FloatBuffer data() {
        return ((FloatBuffer) data.position(0)).slice();
    }

    @Override
    public void divideEquals(final float value) {
        // TODO use blas scal
        // TODO note that there are fewers values to scale
        timesEquals(1.0f / value);
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
        checkRowIndex(row);
        FloatDenseVector v = createRowVector();
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
    public void setColumn(final int column, final FloatVector<?> columnVector) {
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
    public void setRow(final int row, final FloatVector<?> rowVector) {
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
        // TODO use blas scal
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
        return new FloatPackedMatrix(data, rows, columns, packedType.transpose());
    }
}
