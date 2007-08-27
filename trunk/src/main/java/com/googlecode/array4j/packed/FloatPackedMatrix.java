package com.googlecode.array4j.packed;

import java.nio.FloatBuffer;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.dense.FloatDenseVector;

public final class FloatPackedMatrix extends AbstractPackedMatrix<FloatPackedMatrix, FloatDenseVector> implements
        FloatMatrix<FloatPackedMatrix, FloatDenseVector> {
    private final FloatBuffer data;

    public static FloatPackedMatrix createSymmetric(final int rows, final int columns) {
        return new FloatPackedMatrix(rows, columns, PackedType.SYMMETRIC);
    }

    public static FloatPackedMatrix createUpperTriangular(final int rows, final int columns) {
        return new FloatPackedMatrix(rows, columns, PackedType.UPPER_TRIANGULAR);
    }

    public static FloatPackedMatrix createLowerTriangular(final int rows, final int columns) {
        return new FloatPackedMatrix(rows, columns, PackedType.LOWER_TRIANGULAR);
    }

    private FloatPackedMatrix(final int rows, final int columns, final PackedType packedType) {
        super(rows, columns, packedType);
        this.data = null;
    }

    @Override
    public FloatDenseVector column(final int column) {
        // TODO return a dense column
        return null;
    }

    @Override
    public void divideEquals(final float value) {
        // TODO use blas scal
        // TODO note that there are fewers values to scale
    }

    @Override
    public float get(final int row, final int column) {
        return data.get(elementOffset(row, column));
    }

    @Override
    public void minusEquals(final float value) {
    }

    @Override
    public void plusEquals(final float value) {
    }

    @Override
    public FloatDenseVector row(final int row) {
        // TODO return a dense row
        return null;
    }

    @Override
    public void set(final int row, final int column, final float value) {
        data.put(elementOffset(row, column), value);
    }

    @Override
    public void setColumn(final int column, final FloatVector<?> columnVector) {
        // TODO figure out which values to set
        // TODO if working with triangular matrices, make sure columnVector has
        // zeros where it is supposed to, so that it doesn't violate the
        // triangular constraint
    }

    @Override
    public void setRow(final int row, final FloatVector<?> rowVector) {
    }

    @Override
    public void timesEquals(final float value) {
        // TODO use blas scal
    }

    @Override
    public float[] toArray() {
        // TODO figure out if this returns a dense array or a packed array
        // TODO a packed array will probably be more useful
        return null;
    }

    @Override
    public float[][] toColumnArrays() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float[][] toRowArrays() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FloatPackedMatrix transpose() {
        if (isSymmetric()) {
            return this;
        } else {
            // TODO probably need to switch uplo?
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public FloatDenseVector createColumnVector() {
        return new FloatDenseVector(rows(), Orientation.COLUMN, storage());
    }

    @Override
    public FloatDenseVector createRowVector() {
        return new FloatDenseVector(columns(), Orientation.ROW, storage());
    }

    @Override
    public Storage storage() {
        // TODO Auto-generated method stub
        return null;
    }

    public float[] dataArray() {
        return data.array();
    }

    @Override
    public FloatDenseVector asVector() {
        // TODO need to make a dense copy here and return that as a vector
        throw new UnsupportedOperationException();
    }
}