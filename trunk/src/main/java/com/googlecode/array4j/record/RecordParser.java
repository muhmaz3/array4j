package com.googlecode.array4j.record;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class RecordParser {
    public static void main(final String[] args) {
        System.out.println("record parser");

        Class klass = MyRecordType.class;

        // TODO getFields doesn't guarantee ordering
        for (Field field : klass.getFields()) {
            System.out.println(field);
            for (Annotation annotation : field.getAnnotations()) {
                System.out.println(annotation);
                if (field.isAnnotationPresent(Integer.class)) {
                    Integer intAnnotation = field.getAnnotation(Integer.class);
                    System.out.println(intAnnotation.byteOrder());
                }
            }
        }
    }
}
