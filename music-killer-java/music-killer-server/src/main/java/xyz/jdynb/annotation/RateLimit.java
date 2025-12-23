package xyz.jdynb.annotation;

import xyz.jdynb.enums.RateLimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 * 使用示例：
 * @RateLimit(time = 60, count = 10) // 60秒内最多访问10次
 * @RateLimit(time = 1, count = 5, limitType = RateLimitType.IP) // 按IP限流，1秒内最多5次
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流唯一标识的key（默认使用方法全路径）
     */
    String key() default "";

    /**
     * 限流时间窗口，单位：秒
     */
    int time() default 60;

    /**
     * 限流时间窗口内最大请求次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    RateLimitType limitType() default RateLimitType.DEFAULT;

    /**
     * 提示消息
     */
    String message() default "访问过于频繁，请稍后再试";

    /**
     * 运行发生错误后删除 key
     * @return true 则允许
     */
    boolean allowRetry() default false;

    /**
     * SpEL表达式,用于从参数中提取幂等标识
     * 仅当 type = SPEL 时生效
     * 例如: "#request.orderId" 或 "#user.id"
     */
    String expression() default "";
}
