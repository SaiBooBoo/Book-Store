package com.example.bookstore.criteriaQuery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    String propName() default "";

    String joinName() default "";

    String blurry() default "";

    boolean distinct() default false;

    Type type() default Type.EQUAL;

}
