package com.googlecode.array4j.types;

import org.junit.Test;

public final class ArrayDescrTest {
    @Test
    public void test() {
        System.out.println(ArrayDescr.fromType(Types.BOOL));
        System.out.println(ArrayDescr.fromType(Types.BOOL));
//        System.out.println(ArrayDescr.valueOf(Types.CHAR));
        System.out.println(ArrayDescr.fromType(Types.DOUBLE));
    }
}
