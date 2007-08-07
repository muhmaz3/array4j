package com.googlecode.array4j.io;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import com.googlecode.array4j.dense.FloatDenseMatrix;

public final class MatrixIOStreamTest {
    @Test
    public void test() throws IOException {
        FloatDenseMatrix matrix = new FloatDenseMatrix(10, 20);
        matrix.fillRandom(new Random(0));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixOutputStream out = new MatrixOutputStream(baos);
        out.writeMatrix(matrix);
        out.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        MatrixInputStream in = new MatrixInputStream(bais);
        FloatDenseMatrix matrix2 = in.readMatrix();
        assertEquals(matrix, matrix2);
    }
}
