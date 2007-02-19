package com.googlecode.array4j;

import static com.googlecode.array4j.Indexing.ellipsis;
import static com.googlecode.array4j.Indexing.newaxis;
import static com.googlecode.array4j.Indexing.slice;
import static com.googlecode.array4j.Indexing.sliceStep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.Ignore;
import org.junit.Test;

public final class ByteArrayTest {
    @Test
    public void testArange() {
        final ByteArray arr = DenseByteArray.arange(1, 11, 1);
        for (int i = 0; i < 10; i++) {
            assertEquals((byte) (i + 1), arr.get(i));
        }
        for (int i = -1; i >= -10; i--) {
            assertEquals((byte) (11 + i), arr.get(i));
        }
    }

    private <E extends ByteArray> E foo(final ByteArray<E> arr) {
        if (arr == null) {
            // TODO how do we make a new instance of E here?
            // JRuby will have to make a function wrapper...
            return null;
        } else {
            return arr.reshape(3, 2, 1);
        }
    }

    @Test
    public void testSlice() {
        DenseByteArray arr = DenseByteArray.arange(1, 10, 1);
//        arr = arr.reshape(3, 3);
        arr = arr.reshape(9);
        assertNotNull(arr);
//        assertEquals((byte) 9, arr.get(2, 2));
//        arr = arr.get(slice(1, 3), slice(1, 3));
//        assertNotNull(arr);
//        assertEquals((byte) 5, arr.get(0, 0));
//        assertEquals((byte) 6, arr.get(0, 1));
//        assertEquals((byte) 8, arr.get(1, 0));
//        assertEquals((byte) 9, arr.get(1, 1));
    }

    @Ignore
    public void test() {
//        final ByteArray arr = new ByteArray(10);
        final ByteArray arr = null;

        arr.get(10);

        // TODO figure out how to do: arr[1:, 1:] and arr[-1:,-1:]

        // arr[:, ...]
        arr.get(slice(), ellipsis());

        // arr[10, ...]
        arr.get(10, ellipsis());

        // arr[10, 20, 30]
        arr.get(10, 20, 30);

        // two ways to force array return even when using only integer indexes
        arr.get(new Object[]{10, 20, 30});
        arr.get(10, 20, 30, ellipsis());
        arr.getArray(10, 20, 30);

        // arr[10, 20, 30] (not fancy indexing)
        arr.get(new int[]{10, 20, 30});

        // arr[newaxis, 10, 20, ...]
        arr.get(newaxis(), 10, 20, ellipsis());

        // arr[..., ::2]
        arr.get(ellipsis(), sliceStep(2));

        // fancy indexing (returns a copy)
        arr.get(new int[]{3, 1, 2}, ellipsis());
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void testInvalidGetArgument() {
//        final ByteArray arr = new ByteArray(10);
//        arr.get(new Object());
//    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ByteArrayTest.class);
    }
}
