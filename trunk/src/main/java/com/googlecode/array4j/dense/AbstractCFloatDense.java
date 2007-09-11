package com.googlecode.array4j.dense;

import com.googlecode.array4j.ComplexFloat;
import com.googlecode.array4j.ComplexFloatMatrix;
import com.googlecode.array4j.ComplexFloatVector;
import com.googlecode.array4j.Constants;
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

public abstract class AbstractCFloatDense<M extends ComplexFloatMatrix<M, CFloatDenseVector> & DenseMatrix<M, CFloatDenseVector>>
        extends AbstractDenseMatrix<M, CFloatDenseVector, ComplexFloat[]> {
    private static final int DEFAULT_OFFSET = 0;

    private static final int DEFAULT_STRIDE = 1;

    private static final int ELEMENT_SIZE = 2;

    private static final int ELEMENT_SIZE_BYTES = Constants.FLOAT_BYTES;

    protected transient FloatBuffer data;

    /**
     * Constructor for matrix with existing data.
     */
    public AbstractCFloatDense(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(null, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, offset, stride, orientation);
        this.data = data;
        checkData(data);
    }

    /**
     * Constructor for vector with existing data.
     */
    public AbstractCFloatDense(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        this(data, vectorRows(size, orientation), vectorColumns(size, orientation), offset, stride, orientation);
    }

    /**
     * Constructor for new matrix.
     */
    public AbstractCFloatDense(final int rows, final int columns, final Orientation orientation, final Storage storage) {
        super(null, ELEMENT_SIZE, ELEMENT_SIZE_BYTES, rows, columns, DEFAULT_OFFSET, DEFAULT_STRIDE, orientation);
        this.data = BufferUtils.createComplexFloatBuffer(length, storage);
        checkData(data);
    }

    /**
     * Constructor for new vector.
     */
    public AbstractCFloatDense(final int size, final Orientation orientation, final Storage storage) {
        this(vectorRows(size, orientation), vectorColumns(size, orientation), orientation, storage);
    }

    @Override
    public final CFloatDenseVector column(final int column) {
        checkColumnIndex(column);
        return new CFloatDenseVector(data, rows, columnOffset(column), rowStride, Orientation.COLUMN);
    }

    public final void conj() {
        // TODO optimize this
        for (int i = 0; i < length; i++) {
            set(i, get(i).conj());
        }
    }

    @Override
    protected final ComplexFloat[] createArray(final int length) {
        return new ComplexFloat[length];
    }

    @Override
    protected final ComplexFloat[][] createArrayArray(final int length) {
        return new ComplexFloat[length][];
    }

    @Override
    public final CFloatDenseVector createColumnVector() {
        return new CFloatDenseVector(rows, Orientation.COLUMN, storage());
    }

    @Override
    public final CFloatDenseVector createRowVector() {
        return new CFloatDenseVector(columns, Orientation.ROW, storage());
    }

    public final FloatBuffer data() {
        return ((FloatBuffer) data.position(offset)).slice();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractCFloatDense)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (new EqualsBuilder().appendSuper(super.equals(obj)).isEquals()) {
            return false;
        }
        // TODO optimize this
        AbstractCFloatDense<?> other = (AbstractCFloatDense<?>) obj;
        for (int i = 0; i < length; i++) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected final void fillFrom(final ComplexFloat[] dest, final int srcPos) {
        float real = data.get(srcPos);
        float imag = data.get(srcPos + 1);
        ComplexFloat value = ComplexFloat.valueOf(real, imag);
        Arrays.fill(dest, value);
    }

    public final ComplexFloat get(final int index) {
        int offset = elementOffset(index);
        float real = data.get(offset);
        float imag = data.get(offset + 1);
        return ComplexFloat.valueOf(real, imag);
    }

    public final ComplexFloat get(final int row, final int column) {
        int offset = elementOffset(row, column);
        float real = data.get(offset);
        float imag = data.get(offset + 1);
        return ComplexFloat.valueOf(real, imag);
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Storage storage = (Storage) in.readObject();
        this.data = BufferUtils.createComplexFloatBuffer(length, storage);
        // TODO this stuff can fail when there are offsets and strides involved
        for (int i = 0; i < ELEMENT_SIZE * length; i++) {
            data.put(i, in.readFloat());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public final CFloatDenseVector row(final int row) {
        return new CFloatDenseVector(data, columns, rowOffset(row), columnStride, Orientation.ROW);
    }

    public final void set(final int index, final ComplexFloat value) {
        int offset = elementOffset(index);
        data.put(offset, value.real());
        data.put(offset + 1, value.imag());
    }

    public final void set(final int row, final int column, final ComplexFloat value) {
        int offset = elementOffset(row, column);
        data.put(offset, value.real());
        data.put(offset + 1, value.imag());
    }

    public final void setColumn(final int column, final ComplexFloatVector<?> columnVector) {
        checkColumnIndex(column);
        checkColumnVector(columnVector);
        int targetOffset = columnOffset(column);
        int targetStride = rowStride;
        // TODO this could be optimized
        for (int i = 0; i < rows; i++) {
            ComplexFloat value = columnVector.get(i);
            int position = targetOffset + i * targetStride * elementSize;
            data.put(position, value.real());
            data.put(position + 1, value.imag());
        }
    }

    @Override
    protected final void setFrom(final ComplexFloat[] dest, final int destPos, final int srcPos) {
        float real = data.get(srcPos);
        float imag = data.get(srcPos + 1);
        dest[destPos] = ComplexFloat.valueOf(real, imag);
    }

    public final void setRow(final int row, final ComplexFloatVector<?> rowVector) {
        checkRowIndex(row);
        checkRowVector(rowVector);
        AssertUtils.checkArgument(columns == rowVector.length());
        int targetOffset = rowOffset(row);
        int targetStride = columnStride;
        // TODO this could be optimized
        for (int i = 0; i < columns; i++) {
            ComplexFloat value = rowVector.get(i);
            int position = targetOffset + i * targetStride * elementSize;
            data.put(position, value.real());
            data.put(position + 1, value.imag());
        }
    }

    public final Storage storage() {
        return data.isDirect() ? Storage.DIRECT : Storage.HEAP;
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(storage());
        // TODO optimize this
        for (int i = 0; i < length; i++) {
            ComplexFloat value = get(i);
            out.writeFloat(value.real());
            out.writeFloat(value.imag());
        }
        throw new UnsupportedOperationException();
    }
}
