package com.googlecode.array4j.dense;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.googlecode.array4j.FloatBLAS;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.VectorSupport;

public abstract class AbstractFloatDense<M extends FloatMatrix<M, FloatDenseVector>> extends
        AbstractDenseMatrix<M, FloatDenseVector, float[]> {
    protected static FloatBuffer createFloatBuffer(final int size, final Storage storage) {
        if (storage.equals(Storage.DIRECT)) {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(size * (Float.SIZE >>> 3));
            buffer.order(ByteOrder.nativeOrder());
            return buffer.asFloatBuffer();
        } else {
            return FloatBuffer.allocate(size);
        }
    }

    protected final FloatBuffer data;

    /**
     * Constructor for matrix with existing data.
     */
    public AbstractFloatDense(FloatBuffer data, int rows, int columns, int offset, int stride, Orientation orientation) {
        super(1, rows, columns, offset, stride, orientation);
        this.data = data;
    }

    /**
     * Constructor for vector with existing data.
     */
    public AbstractFloatDense(FloatBuffer data, int size, int offset, int stride, Orientation orientation) {
        this(data, VectorSupport.rows(size, orientation), VectorSupport.columns(size, orientation), offset, stride,
                orientation);
    }

    /**
     * Constructor for new matrix.
     */
    public AbstractFloatDense(int rows, int columns, Orientation orientation, Storage storage) {
        super(1, rows, columns, 0, 1, orientation);
        this.data = createFloatBuffer(size, storage);
    }

    /**
     * Constructor for new vector.
     */
    public AbstractFloatDense(int size, Orientation orientation, Storage storage) {
        this(VectorSupport.rows(size, orientation), VectorSupport.columns(size, orientation), orientation, storage);
    }

    @Override
    protected final float[] createArray(final int length) {
        return new float[length];
    }

    @Override
    protected final float[][] createArrayArray(final int length) {
        return new float[length][];
    }

    public final void fill(final float value) {
        FloatBuffer xdata = createFloatBuffer(1, storage());
        xdata.put(value);
        FloatDenseVector x = new FloatDenseVector(xdata, size, 0, 0, Orientation.DEFAULT_FOR_VECTOR);
        FloatBLAS.copy(x, asVector());
    }

    public final float get(final int index) {
        return data.get(elementOffset(index));
    }

    public final float get(final int row, final int column) {
        return data.get(elementOffset(row, column));
    }

    public final Storage storage() {
        return data.isDirect() ? Storage.DIRECT : Storage.JAVA;
    }

    public final void set(final int index, final float value) {
        data.put(elementOffset(index), value);
    }

    public final void set(final int row, final int column, final float value) {
        data.put(elementOffset(row, column), value);
    }

    public final void setColumn(final int column, final FloatVector<?> columnVector) {
        if (!(columnVector instanceof FloatDenseVector)) {
            throw new UnsupportedOperationException();
        }
        // TODO similar code is duplicated in setRow
        final FloatDenseVector x = (FloatDenseVector) columnVector;
        int targetOffset = columnOffset(column);
        int targetStride = rowStride;
        for (int i = 0; i < size; i++) {
            data.put(targetOffset + i * targetStride, x.data.get(i * stride));
        }
    }

    @Override
    protected final void setFrom(final float[] dest, final int destPos, final int srcPos) {
        dest[destPos] = data.get(srcPos);
    }

    public final void setRow(final int row, final FloatVector<?> rowVector) {
        if (!(rowVector instanceof FloatDenseVector)) {
            throw new UnsupportedOperationException();
        }
        // TODO similar code is duplicated in setColumn
        final FloatDenseVector x = (FloatDenseVector) rowVector;
        int targetOffset = rowOffset(row);
        int targetStride = columnStride;
        for (int i = 0; i < size; i++) {
            data.put(targetOffset + i * targetStride, x.data.get(i * stride));
        }
    }

    public final void timesEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    public final float[] dataAsArray() {
        return data.array();
    }

    public final int stride() {
        return stride;
    }

    public final int offset() {
        return offset;
    }

    @Override
    public final FloatDenseVector createColumnVector() {
        return new FloatDenseVector(rows, Orientation.COLUMN, storage());
    }

    @Override
    public final FloatDenseVector createRowVector() {
        return new FloatDenseVector(columns, Orientation.ROW, storage());
    }

    @Override
    public final FloatDenseVector column(final int column) {
        checkColumnIndex(column);
        return new FloatDenseVector(data, rows, columnOffset(column), rowStride, orientation.COLUMN);
    }

    @Override
    public final FloatDenseVector row(final int row) {
        checkRowIndex(row);
        return new FloatDenseVector(data, columns, rowOffset(row), columnStride, orientation.ROW);
    }
}
