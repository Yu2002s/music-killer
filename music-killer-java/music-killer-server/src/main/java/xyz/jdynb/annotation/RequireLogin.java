package xyz.jdynb.annotation;

import java.lang.annotation.*;

/**
 * 用于标识接口必须登录
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireLogin {
}
