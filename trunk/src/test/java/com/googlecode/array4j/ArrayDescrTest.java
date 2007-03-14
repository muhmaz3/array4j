package com.googlecode.array4j;

import org.junit.Test;

import com.googlecode.array4j.ArrayDescr;
import com.googlecode.array4j.ArrayType;

public final class ArrayDescrTest {
    @Test
    public void test() {
        System.out.println(ArrayDescr.fromType(ArrayType.BOOL));
        System.out.println(ArrayDescr.fromType(ArrayType.BOOL));
//        System.out.println(ArrayDescr.valueOf(Types.CHAR));
        System.out.println(ArrayDescr.fromType(ArrayType.DOUBLE));
    }
}
