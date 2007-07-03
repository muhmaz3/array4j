package com.googlecode.array4j;

public final class DirectFloatMatrixTest extends AbstractFloatMatrixTest {
    private static class DirectFloatMatrixFactory implements FloatMatrixFactory {
        public FloatMatrix<?, ?> createMatrix(final float[] data, final int rows, final int columns, final int offset,
                final int stride, final Orientation orientation) {
            return new DirectFloatMatrix(data, rows, columns, offset, stride, orientation);
        }

        public FloatMatrix<?, ?> createMatrix(final float[] data, final int rows, final int columns,
                final Orientation orientation) {
            return new DirectFloatMatrix(data, rows, columns, orientation);
        }

        public FloatMatrix<?, ?> createMatrix(final int rows, final int columns) {
            return new DirectFloatMatrix(rows, columns);
        }

        public FloatMatrix<?, ?> createMatrix(final int rows, final int columns, final Orientation orientation) {
            return new DirectFloatMatrix(rows, columns, orientation);
        }
    }

    public DirectFloatMatrixTest() {
        super(new DirectFloatMatrixFactory());
    }
}
