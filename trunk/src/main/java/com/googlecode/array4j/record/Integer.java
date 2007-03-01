package com.googlecode.array4j.record;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.googlecode.array4j.ByteOrder;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Integer {
    ByteOrder byteOrder() default ByteOrder.NATIVE;
}
