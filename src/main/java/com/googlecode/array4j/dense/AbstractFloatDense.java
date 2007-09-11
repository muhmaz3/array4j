package com.googlecode.array4j.dense;

import com.googlecode.array4j.Constants;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.FloatVector;
import com.googlecode.array4j.Orientation;
import com.googlecode.array4j.Storage;
import com.googlecode.array4j.util.AssertUtils;
import com.googlecode.array4j.util.BufferUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.FloatBuffer;
import java.util.Arrays;
import org.apache.commons.lang.builder.EqualsBuilder;

public abstract class AbstractFloatDense<M extends FloatMatrix<M, FloatDenseVector> & DenseMatrix<M, FloatDenseVector>>
        extends AbstractDenseMatrix<M, FloatDenseVector, float[]> {
    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_STRIDE = 1;

    private static final int ELEMENT_SIZE = 1;

    private static final int ELEMENT_SIZE_BYTES = Constants.FLOAT_BYTES;

    private static FloatBuffer createBuffer(final int size, final Storage storage) {
        return BufferUtils.createFloatBuffer(size, storage);
    }

    protected transient FloatBuffer data;

    public AbstractFloatDense(final AbstractFloatDense<?> base, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(base, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, orientation);
        this.data = base.data;
        checkData(data);
    }

    public AbstractFloatDense(final AbstractFloatDense<?> base, final int size, final int offset, final int stride,
            final Orientation orientation) {
        this(base, vectorRows(size, orientation), vectorColumns(size, orientation), offset, stride, orientation);
    }

    public AbstractFloatDense(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(null, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, orientation);
        this.data = data;
        checkData(data);
    }

    public AbstractFloatDense(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        this(data, vectorRows(size, orientation), vectorColumns(size, orientation), offset, stride, orientation);
    }

    /**
     * Constructor for new matrix.
     */
    public AbstractFloatDense(final int rows, final int columns, final Orientation orientation, final Storage storage) {
        this(createBuffer(rows * columns, storage), rows, columns, DEFAULT_OFFSET, DEFAULT_STRIDE, orientation);
    }

    /**
     * Constructor for new vector.
     */
    public AbstractFloatDense(final int size, final Orientation orientation, final Storage storage) {
        this(vectorRows(size, orientation), vectorColumns(size, orientation), orientation, storage);
    }

    @Override
    public final FloatDenseVector column(final int column) {
        checkColumnIndex(column);
        return new FloatDenseVector(this, rows, columnOffset(column), rowStride, Orientation.COLUMN);
    }

    @Override
    protected final float[] createArray(final int length) {
        return new float[length];
    }

    @Override
    protected final float[][] createArrayArray(final int length) {
        return new float[length][];
    }

    @Override
    public final FloatDenseVector createColumnVector() {
        return new FloatDenseVector(rows, Orientation.COLUMN, storage());
    }

    @Override
    public final FloatDenseVector createRowVector() {
        return new FloatDenseVector(columns, Orientation.ROW, storage());
    }

    public final FloatBuffer data() {
        return ((FloatBuffer) data.position(offset)).slice();
    }

    public final void divideEquals(final float value) {
        timesEquals(1.0f / value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractFloatDense)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!new EqualsBuilder().appendSuper(super.equals(obj)).isEquals()) {
            return false;
        }
        // TODO optimize this
        AbstractFloatDense<?> other = (AbstractFloatDense<?>) obj;
        for (int i = 0; i < length; i++) {
            if (get(i) != other.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected final void fillFrom(final float[] dest, final int srcPos) {
        Arrays.fill(dest, data.get(srcPos));
    }

    public final float get(final int index) {
        return data.get(elementOffset(index));
    }

    public final float get(final int row, final int column) {
        return data.get(elementOffset(row, column));
    }

    public final void minusEquals(final float value) {
        plusEquals(-1.0f * value);
    }

    public final void plusEquals(final float value) {
        // TODO optimize this
        for (int i = 0; i < length; i++) {
            set(i, get(i) + value);
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Storage storage = (Storage) in.readObject();
        this.data = createBuffer(length, storage);
        checkData(data);
        // TODO this stuff can fail when there are offsets and strides involved
//        for (int i = 0; i < length; i++) {
//            data.put(i, in.readFloat());
//        }
        throw new UnsupportedOperationException();
    }

    @Override
    public final FloatDenseVector row(final int row) {
        return new FloatDenseVector(this, columns, rowOffset(row), columnStride, Orientation.ROW);
    }

    public final void set(final int index, final float value) {
        data.put(elementOffset(index), value);
    }

    public final void set(final int row, final int column, final float value) {
        data.put(elementOffset(row, column), value);
    }

    public final void setColumn(final int column, final FloatVector<?> columnVector) {
        // TODO this code is almost identical to setRow
        checkColumnIndex(column);
        AssertUtils.checkArgument(rows == columnVector.rows());
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

    public final void setRow(final int row, final FloatVector<?> rowVector) {
        // TODO this code is almost identical to setColumn
        checkRowIndex(row);
        AssertUtils.checkArgument(columns == rowVector.columns());
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

    public final void timesEquals(final float value) {
        // TODO optimize this
        for (int i = 0; i < length; i++) {
            set(i, get(i) * value);
        }
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(storage());
//        for (int i = 0; i < length; i++) {
//            out.writeFloat(get(i));
//        }
        throw new UnsupportedOperationException();
    }
}
