package com.googlecode.array4j.dense;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.Direction;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Order;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.util.BufferUtils;
import java.nio.FloatBuffer;
import java.util.Arrays;

abstract class AbstractFloatDense extends AbstractDenseMatrix<FloatDenseVector, float[]> {
    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_STRIDE = 1;

    private static final int ELEMENT_SIZE = 1;

    private static final int ELEMENT_SIZE_BYTES = Constants.FLOAT_BYTES;

    private static FloatBuffer createBuffer(final int size, final Storage storage) {
        return BufferUtils.createFloatBuffer(size, storage);
    }

    public AbstractFloatDense(final AbstractFloatDense base, final int rows, final int columns, final int offset,
            final int stride, final Order order) {
        super(base, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, order);
        checkData(base.data);
        this.data = base.data;
    }

    public AbstractFloatDense(final AbstractFloatDense base, final int size, final int offset, final int stride,
            final Direction direction) {
        this(base, vectorRows(size, direction), vectorColumns(size, direction), offset, stride, direction.order());
    }

    public AbstractFloatDense(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Order order) {
        super(null, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, order);
        checkData(data);
        this.data = data;
    }

    public AbstractFloatDense(final FloatBuffer data, final int size, final int offset, final int stride,
            final Direction direction) {
        this(data, vectorRows(size, direction), vectorColumns(size, direction), offset, stride, direction.order());
    }

    /**
     * Constructor for new matrix.
     */
    public AbstractFloatDense(final int rows, final int columns, final Order orientation, final Storage storage) {
        this(createBuffer(rows * columns, storage), rows, columns, DEFAULT_OFFSET, DEFAULT_STRIDE, orientation);
    }

    /**
     * Constructor for new vector.
     */
    public AbstractFloatDense(final int size, final Direction direction, final Storage storage) {
        this(vectorRows(size, direction), vectorColumns(size, direction), direction.order(), storage);
    }

    protected transient FloatBuffer data;

    @Override
    protected final float[] createArray(final int length) {
        return new float[length];
    }

    @Override
    public final FloatDenseVector column(final int column) {
        return new FloatDenseVectorImpl(this, rows, columnOffset(column), rowStride, Direction.COLUMN);
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
        return new FloatDenseVectorImpl(this, columns, rowOffset(row), columnStride, Direction.ROW);
    }

    public final void divideEquals(final float value) {
        timesEquals(1.0f / value);
    }

    public final void minusEquals(final float value) {
        plusEquals(-value);
    }

    public final void plusEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    public final void timesEquals(final float value) {
        throw new UnsupportedOperationException();
    }

    protected static void copy(final FloatMatrix src, final FloatDenseMatrix dest) {
        // TODO optimize this
        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.columns(); j++) {
                dest.set(i, j, src.get(i, j));
            }
        }
    }

    public void plusEquals(final FloatMatrix other) {
//        if (other instanceof CopyOfFloatDenseVector) {
//            FloatDenseBLAS.DEFAULT.axpy(1.0f, (CopyOfFloatDenseVector) other, this);
//        } else {
//            if (length != other.length()) {
//                throw new IllegalArgumentException();
//            }
//            for (int i = 0; i < length; i++) {
//                set(i, get(i) + other.get(i));
//            }
//        }
    }
}
