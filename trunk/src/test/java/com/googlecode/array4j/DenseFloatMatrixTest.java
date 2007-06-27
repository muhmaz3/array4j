package com.googlecode.array4j;

import com.googlecode.array4j.DenseFloatMatrix;
import com.googlecode.array4j.FloatMatrix;
import com.googlecode.array4j.Orientation;

public final class DenseFloatMatrixTest extends AbstractFloatMatrixTest {
    private static class DenseFloatMatrixFactory implements FloatMatrixFactory {
        public FloatMatrix<?, ?> createMatrix(final int rows, final int columns) {
            return new DenseFloatMatrix(rows, columns);
        }

        public FloatMatrix<?, ?> createMatrix(final int rows, final int columns, final Orientation orientation) {
            return new DenseFloatMatrix(rows, columns, orientation);
        }

        public FloatMatrix<?, ?> createMatrix(final float[] data, final int rows, final int columns, final int offset,
                final int stride, final Orientation orientation) {
            return new DenseFloatMatrix(data, rows, columns, offset, stride, orientation);
        }

        public FloatMatrix<?, ?> createMatrix(final float[] data, final int rows, final int columns,
                final Orientation orientation) {
            return new DenseFloatMatrix(data, rows, columns, orientation);
        }
    }

    public DenseFloatMatrixTest() {
        super(new DenseFloatMatrixFactory());
    }
}
