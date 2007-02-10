package com.googlecode.array4j;

import static com.googlecode.array4j.Indexing.ellipsis;
import static com.googlecode.array4j.Indexing.slice;

import org.junit.Test;

public final class ByteArrayTest {
    @Test
    public void test() {
        final ByteArray arr = new ByteArray();
        arr.get(slice(), ellipsis());
    }
}
