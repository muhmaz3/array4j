package com.googlecode.array4j;

import java.nio.FloatBuffer;
import java.util.Arrays;

import com.googlecode.array4j.internal.ToArraysConverter;

public final class DirectFloatVector extends AbstractDenseVector<DirectFloatVector, float[]> implements
        FloatVector<DirectFloatVector>, DenseVector<DirectFloatVector> {
    private final FloatBuffer data;

    DirectFloatVector(final FloatBuffer data, final int size, final int offset, final int stride,
            final Orientation orientation) {
        super(size, offset, stride, orientation);
        this.data = data;
    }

    public DirectFloatVector(final int size) {
        this(size, 0, 1, Orientation.DEFAULT_FOR_VECTOR);
    }

    public DirectFloatVector(final int size, final int offset, final int stride, final Orientation orientation) {
        this(DirectFloatMatrix.createBuffer(size), size, offset, stride, orientation);
    }

    protected ToArraysConverter<DirectFloatVector, float[]> createArraysConverter() {
        return new ToArraysConverter<DirectFloatVector, float[]>(this) {
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
        // TODO code duplicated from DirectFloatMatrix.getData
        return (FloatBuffer) ((FloatBuffer) data.rewind()).position(offset);
    }

    public float[] toArray() {
        // TODO code duplicated from DirectFloatMatrix.toArray
        float[] arr = new float[size];
        if (size == 0) {
            return arr;
        }
        if (stride == 0) {
            Arrays.fill(arr, getData().get(0));
            return arr;
        }
        FloatBuffer src = getData();
        for (int i = offset, j = 0; j < size; i += stride, j++) {
            arr[j] = src.get(i);
        }
        return arr;
    }
}
