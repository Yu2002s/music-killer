package xyz.jdynb.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER})
public @interface Test {

    int value() default 0;

}
