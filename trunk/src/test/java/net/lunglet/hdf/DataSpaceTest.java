package net.lunglet.hdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public final class DataSpaceTest {
    @Test
    public void testOffsetSimple() {
        DataSpace space = new DataSpace(1L);
        // TODO this doesn't work, even though the documentation says it should
//        space.offsetSimple(null);
    }

    @Test
    public void testSelectElements() {
        DataSpace space = new DataSpace(3, 4, 2);
        long[] dims = space.getDims();
        assertEquals(3, dims.length);
        assertTrue(Arrays.equals(new long[]{3, 4, 2}, dims));
        List<Point> pointList = new ArrayList<Point>();
        for (long i = 0; i < dims[0]; i++) {
            for (long j = 0; j < dims[1]; j++) {
                for (long k = 0; k < dims[2]; k++) {
                    pointList.add(new Point(i, j, k));
                    Point[] points = pointList.toArray(new Point[0]);
                    space.selectElements(SelectionOperator.SET, points);
                    assertEquals(points.length, space.getSelectNPoints());
                    Point[] selectedPoints = space.getSelectedPoints();
                    assertEquals(points.length, selectedPoints.length);
                    for (Point point : selectedPoints) {
                        assertTrue(pointList.contains(point));
                    }
                }
            }
        }
    }

    @Test
    public void testSelectHyperslab() {
        long i = 3;
        long j = 1;
        long k = 2;
        DataSpace space = new DataSpace(i, j, k);
        SelectionOperator op = SelectionOperator.SET;
        long[] start = {0, 0, 0};
        long[] stride = {1, 1, 1};
        long[] count = {1, 1, 1};
        long[] block = {2, 1, 2};
        space.selectHyperslab(op, start, stride, count, block);
        assertEquals(SelectionType.HYPERSLABS, space.getSelectType());
        assertEquals(4, space.getSelectNPoints());
        Point[] bounds = space.getSelectBounds();
        assertEquals(2, bounds.length);
        Point startPoint = new Point(0, 0, 0);
        assertEquals(startPoint, bounds[0]);
        Point endPoint = new Point(1, 0, 1);
        assertEquals(endPoint, bounds[1]);
        assertEquals(1, space.getSelectHyperNBlocks());
        Hyperslab[] slabs = space.getHyperslabs();
        assertEquals(1, slabs.length);
        assertEquals(startPoint, slabs[0].getStartPoint());
        assertEquals(endPoint, slabs[0].getEndPoint());
        space.close();
    }

    @Test
    public void testSelectNoneAll() {
        long i = 3;
        long j = 1;
        long k = 2;
        long n = i * j * k;
        DataSpace space = new DataSpace(i, j, k);
        // check that everything is selected by default
        assertEquals(SelectionType.ALL, space.getSelectType());
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
}
