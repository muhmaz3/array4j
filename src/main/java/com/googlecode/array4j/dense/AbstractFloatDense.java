package com.googlecode.array4j.dense;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.util.BufferUtils;
import java.nio.FloatBuffer;
import java.util.Arrays;

abstract class AbstractFloatDense extends AbstractDenseMatrix<FloatDenseVector, float[]> {
    public AbstractFloatDense() {
        super(null, 0, 0, 0, 0, 0, 0, null);
    }

    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_STRIDE = 1;

    private static final int ELEMENT_SIZE = 1;

    private static final int ELEMENT_SIZE_BYTES = Constants.FLOAT_BYTES;

    private static FloatBuffer createBuffer(final int size, final Storage storage) {
        return BufferUtils.createFloatBuffer(size, storage);
    }

//    public CopyOfAbstractFloatDense(final CopyOfAbstractFloatDense<?> base, final int rows, final int columns, final int offset,
//            final int stride, final Order orientation) {
//        super(base, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, orientation);
//        checkData(base.data);
//        this.data = base.data;
//    }
//
//    public CopyOfAbstractFloatDense(final CopyOfAbstractFloatDense<?> base, final int size, final int offset, final int stride,
//            final Order orientation) {
//        this(base, vectorRows(size, orientation), vectorColumns(size, orientation), offset, stride, orientation);
//    }
//
//    public CopyOfAbstractFloatDense(final FloatBuffer data, final int rows, final int columns, final int offset,
//            final int stride, final Order orientation) {
//        super(null, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, orientation);
//        checkData(data);
//        this.data = data;
//    }
//
//    public CopyOfAbstractFloatDense(final FloatBuffer data, final int size, final int offset, final int stride,
//            final Order orientation) {
//        this(data, vectorRows(size, orientation), vectorColumns(size, orientation), offset, stride, orientation);
//    }
//
//    /**
//     * Constructor for new matrix.
//     */
//    public CopyOfAbstractFloatDense(final int rows, final int columns, final Order orientation, final Storage storage) {
//        this(createBuffer(rows * columns, storage), rows, columns, DEFAULT_OFFSET, DEFAULT_STRIDE, orientation);
//    }
//
//    /**
//     * Constructor for new vector.
//     */
//    public CopyOfAbstractFloatDense(final int size, final Order orientation, final Storage storage) {
//        this(vectorRows(size, orientation), vectorColumns(size, orientation), orientation, storage);
//    }

    protected transient FloatBuffer data;

    @Override
    protected final float[] createArray(final int length) {
        return new float[length];
    }

    @Override
    public final FloatDenseVector column(final int column) {
//        return new FloatDenseVectorImpl(this, rows, columnOffset(column), rowStride, Order.COLUMN);
        return null;
    }

    @Override
    protected final float[][] createArrayArray(final int length) {
        return new float[length][];
    }

    public final FloatBuffer data() {
        return ((FloatBuffer) data.position(offset)).slice();
    }

    @Override
    protected final void fillFrom(final float[] dest, final int srcPos) {
        Arrays.fill(dest, data.get(srcPos));
    }

    public final float get(final int index) {
        return data.get(elementOffset(index));
    }

    public final void setRow(final int row, final FloatVector rowVector) {
        checkRowIndex(row);
        checkRowVector(rowVector);
        int targetOffset = rowOffset(row);
        int targetStride = columnStride;
        // TODO this could be optimized
        for (int i = 0; i < columns; i++) {
            data.put(targetOffset + i * targetStride, rowVector.get(i));
        }
    }

    public final Storage storage() {
        return data.isDirect() ? Storage.DIRECT : Storage.HEAP;
    }

    public final float get(final int row, final int column) {
        return data.get(elementOffset(row, column));
    }

    public final void set(final int index, final float value) {
        data.put(elementOffset(index), value);
    }

    public final void setColumn(final int column, final FloatVector columnVector) {
        checkColumnIndex(column);
        checkColumnVector(columnVector);
        int targetOffset = columnOffset(column);
        int targetStride = rowStride;
        // TODO this could be optimized
        for (int i = 0; i < rows; i++) {
            data.put(targetOffset + i * targetStride, columnVector.get(i));
        }
    }

    @Override
    protected final void setFrom(final float[] dest, final int destPos, final int srcPos) {
        dest[destPos] = data.get(srcPos);
    }

    public final void set(final int row, final int column, final float value) {
        data.put(elementOffset(row, column), value);
    }

    @Override
    public final FloatDenseVector row(final int row) {
//        return new FloatDenseVectorImpl(this, columns, rowOffset(row), columnStride, Order.ROW);
        return null;
    }

    public boolean isColumnVector() {
        return rows <= 1;
    }

    public boolean isRowVector() {
        return columns <= 1;
    }

    public int length() {
        return super.length();
    }
}
