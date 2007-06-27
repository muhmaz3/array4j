package com.googlecode.array4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import com.googlecode.array4j.internal.ToArraysConverter;

public final class DirectFloatMatrix extends AbstractDenseMatrix<DirectFloatMatrix, DirectFloatVector, float[]>
        implements FloatMatrix<DirectFloatMatrix, DirectFloatVector>, DenseMatrix<DirectFloatMatrix, DirectFloatVector> {
    private static final int FLOAT_SIZE = Float.SIZE >>> 3;

    static FloatBuffer createBuffer(final float[] values) {
        FloatBuffer buffer = createBuffer(values.length * FLOAT_SIZE);
        buffer.put(values);
        return buffer;
    }

    static FloatBuffer createBuffer(final int size) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(size * FLOAT_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        return buffer.asFloatBuffer();
    }

    final FloatBuffer data;

    private final transient DenseFloatSupport<DirectFloatMatrix, DirectFloatVector> floatSupport;

    public DirectFloatMatrix(final float[] data, final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        this(createBuffer(data), rows, columns, offset, stride, orientation);
    }

    public DirectFloatMatrix(final float[] values, final int rows, final int columns, final Orientation orientation) {
        this(values, rows, columns, 0, 1, orientation);
    }

    private DirectFloatMatrix(final FloatBuffer data, final int rows, final int columns, final int offset,
            final int stride, final Orientation orientation) {
        super(rows, columns, offset, stride, orientation);
        this.data = data;
        checkPostcondition(getData().remaining() >= size);
        // TODO don't copy data here
        this.floatSupport = new DenseFloatSupport<DirectFloatMatrix, DirectFloatVector>(this, toArray());
    }

    public DirectFloatMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.ROW);
    }

    public DirectFloatMatrix(final int rows, final int columns, final Orientation orientation) {
        this(createBuffer(rows * columns), rows, columns, 0, 1, orientation);
    }

    protected ToArraysConverter<DirectFloatMatrix, float[]> createArraysConverter() {
        return new ToArraysConverter<DirectFloatMatrix, float[]>(this) {
            @Override
            protected float[] createArray(final int length) {
                return new float[length];
            }

            @Override
            protected float[][] createArrayArray(final int length) {
                return new float[length][];
            }

            @Override
            protected void set(final int srcPos, final float[] dest, final int destPos) {
                dest[destPos] = data.get(srcPos);
            }
        };
    }

    public DirectFloatVector createSharingVector(final int size, final int offset, final int stride,
            final Orientation orientation) {
        return new DirectFloatVector(getData(), size, offset, stride, orientation);
    }

    private FloatBuffer getData() {
        return (FloatBuffer) ((FloatBuffer) data.rewind()).position(offset);
    }

    public void setColumn(final int column, final FloatVector columnVector) {
        floatSupport.setColumn(column, columnVector);
    }

    public void setRow(final int row, final FloatVector rowVector) {
        floatSupport.setRow(row, rowVector);
    }

    public float[] toArray() {
        float[] arr = new float[size];
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            // TODO should getData take care of positioning the buffer at offset?
            Arrays.fill(arr, getData().get(offset));
            return arr;
        }
        FloatBuffer src = getData();
        for (int i = offset, j = 0; j < size; i += stride, j++) {
            arr[j] = src.get(i);
        }
        return arr;
    }

    public DirectFloatMatrix transpose() {
        // interchange columns and rows and change orientation
        return new DirectFloatMatrix(getData(), columns, rows, offset, stride, orientation.transpose());
    }
}
