package com.googlecode.array4j;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

import com.googlecode.array4j.AbstractDenseMatrix.ToArraysConverter;


public final class DirectFloatMatrix extends AbstractDenseMatrix<DirectFloatMatrix, DirectFloatVector> implements
        FloatMatrix<DirectFloatMatrix, DirectFloatVector>, DenseMatrix<DirectFloatMatrix, DirectFloatVector> {
    private static final int FLOAT_SIZE = Float.SIZE >>> 3;

    private final transient ToArraysConverter<float[]> arraysConverter;

    final FloatBuffer data;

    public DirectFloatMatrix(final float[] values, final int rows, final int columns, final Orientation orientation) {
        this(rows, columns, orientation);
        getData().put(values);
    }

    public DirectFloatMatrix(final int rows, final int columns) {
        this(rows, columns, Orientation.ROW);
    }

    public DirectFloatMatrix(final int rows, final int columns, final Orientation orientation) {
        super(rows, columns, 0, 1, orientation);
        ByteBuffer buffer = ByteBuffer.allocateDirect(size * FLOAT_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        this.data = buffer.asFloatBuffer();
        this.arraysConverter = createArraysConverter();
    }

    public DirectFloatMatrix(final float[] data, final int rows, final int columns, final int offset, final int stride,
            final Orientation orientation) {
        super(rows, columns, offset, stride, orientation);
        ByteBuffer buffer = ByteBuffer.allocateDirect(size * FLOAT_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        this.data = buffer.asFloatBuffer();
        this.data.put(data);
        this.arraysConverter = createArraysConverter();
    }

    private ToArraysConverter<float[]> createArraysConverter() {
        return new ToArraysConverter<float[]>() {
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

    private FloatBuffer getData() {
        return (FloatBuffer) data.rewind();
    }

    public DirectFloatVector row(final int row) {
        return null;
    }

    public float[] toArray() {
        float[] arr = new float[size];
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            Arrays.fill(arr, getData().get(offset));
            return arr;
        }
        FloatBuffer src = getData();
        for (int i = offset, j = 0; j < size; i += stride, j++) {
            arr[j] = src.get(i);
        }
        return arr;
    }

    public float[][] toColumnArrays() {
        return arraysConverter.toArrays(columns, rows, false);
    }

    public float[][] toRowArrays() {
        return arraysConverter.toArrays(rows, columns, true);
    }
}
