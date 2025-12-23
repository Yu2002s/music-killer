package xyz.jdynb.annotation;

import xyz.jdynb.enums.OperationType;

import java.lang.annotation.*;

/**
 * 自动填充数据库字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoFill {

    /**
     * 操作类型
     * @return 具体类型
     */
    OperationType value();

}
