package com.googlecode.array4j;

import static com.googlecode.array4j.Indexing.ellipsis;
import static com.googlecode.array4j.Indexing.newaxis;
import static com.googlecode.array4j.Indexing.slice;
import static com.googlecode.array4j.Indexing.sliceStep;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

public final class ByteArrayTest {
    @Test
    public void test() {
        final ByteArray arr = new ByteArray(10);

        // arr[:, ...]
        arr.get(slice(), ellipsis());

        // arr[10, ...]
        arr.get(10, ellipsis());

        // arr[10, 20, 30]
        arr.get(10, 20, 30);

        // arr[10, 20, 30] (not fancy indexing)
//        arr.get(new int[]{10, 20, 30});

        // arr[newaxis, 10, 20, ...]
        arr.get(newaxis(), 10, 20, ellipsis());

        // arr[..., ::2]
        arr.get(ellipsis(), sliceStep(2));

        // fancy indexing (returns a copy)
        arr.get(new int[]{3, 1, 2}, ellipsis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGetArgument() {
        final ByteArray arr = new ByteArray(10);
        arr.get(new Object());
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ByteArrayTest.class);
    }
}
