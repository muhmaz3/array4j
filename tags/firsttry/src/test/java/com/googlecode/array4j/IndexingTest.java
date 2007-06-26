package com.googlecode.array4j;

import static com.googlecode.array4j.Indexing.sliceStep;
import static com.googlecode.array4j.Indexing.slice;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public final class IndexingTest {
    @Test(expected = IllegalArgumentException.class)
    public void testZeroSliceStep() {
        sliceStep(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSliceZeroStep() {
        slice(0, 0, 0);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IndexingTest.class);
    }
}
