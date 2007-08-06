package com.googlecode.array4j.packed;

import java.nio.FloatBuffer;
import java.util.Random;

import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.dense.FloatDenseVector;

// TODO use ?syrk to calculate a symmetric matrix

public final class FloatPackedMatrix extends AbstractPackedMatrix<FloatPackedMatrix, FloatDenseVector> implements
        FloatMatrix<FloatPackedMatrix, FloatDenseVector> {
    private final FloatBuffer data;

    public FloatPackedMatrix(final int rows, final int columns) {
        super(rows, columns);
        this.data = null;
    }

    @Override
    public void divideEquals(final float value) {
        // TODO use blas scal
        // TODO note that there are fewers values to scale
    }

    @Override
    public void fill(final float value) {
        // TODO use blas copy
    }

    // TODO move this method out of the matrix interface
    @Override
    public void fillRandom(final Random rng) {
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
    public FloatMatrix<?, ?> times(final FloatMatrix<?, ?> matrix) {
        // TODO use BLAS level 3 ?symm
        return null;
    }

    @Override
    public void timesEquals(final float value) {
        // TODO use blas scal
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
    public FloatDenseVector asVector() {
        // TODO return a dense vector
        return null;
    }

    @Override
    public FloatDenseVector column(final int column) {
        // TODO return a dense column
        return null;
    }

    @Override
    public FloatDenseVector createColumnVector() {
        // TODO use same storage as matrix
        return new FloatDenseVector(rows, Orientation.COLUMN, null);
    }

    @Override
    public FloatDenseVector createRowVector() {
        return new FloatDenseVector(columns, Orientation.ROW, null);
    }

    @Override
    public FloatDenseVector row(final int row) {
        // TODO return a dense row
        return null;
    }

    @Override
    public FloatPackedMatrix subMatrixColumns(final int column0, final int column1) {
        // TODO this makes a matrix that is no longer symmetric, so this
        // function should probably be moved to a DenseMatrix interface that is
        // generified so that FloatPackedMatrix can implement it to return
        // FloatDenseMatrix
        throw new UnsupportedOperationException();
    }

    @Override
    public FloatPackedMatrix transpose() {
        // TODO switch uplo
        return null;
    }

    @Override
    public float[] toArray() {
        // TODO figure out if this returns a dense array or a packed array
        // TODO a packed array will probably be more useful
        return null;
    }
}
