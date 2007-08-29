package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class DataSpaceTest {
    @Test
    public void testSelectNoneAll() {
        long i = 3;
        long j = 1;
        long k = 2;
        long n = i * j * k;
        DataSpace space = new DataSpace(i, j, k);
        space.selectNone();
        assertEquals(SelectionType.NONE, space.getSelectType());
        assertEquals(0, space.getSelectNPoints());
        space.selectAll();
        assertEquals(SelectionType.ALL, space.getSelectType());
        assertEquals(n, space.getSelectNPoints());
        Point[] bounds = space.getSelectBounds();
        assertEquals(2, bounds.length);
        assertEquals(new Point(0, 0, 0), bounds[0]);
        assertEquals(new Point(i - 1, j - 1, k - 1), bounds[1]);
        space.close();
    }

    @Test
    public void testOffsetSimple() {
        DataSpace space = new DataSpace(1L);
        // TODO this doesn't work, even though the documentation says it should
//        space.offsetSimple(null);
    }

//  SelectionOperator op = SelectionOperator.H5S_SELECT_SET;
//  long[] start = {0, 0, 0};
//  long[] stride = {1, 1, 1};
//  long[] count = {1, 1, 1};
//  long[] block = {1, 1, 1};
//  space.selectHyperslab(op, start, stride, count, block);
}
