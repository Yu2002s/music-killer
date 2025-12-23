package xyz.jdynb.annotation;

import xyz.jdynb.enums.IdempotentType;

import java.lang.annotation.*;

/**
 * 幂等性注解
 * 用于防止接口重复提交,保证相同请求多次调用与单次调用效果相同
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 幂等Key前缀,用于区分不同业务场景
     * 默认为空,使用方法签名作为key
     */
    String key() default "";

    /**
     * 幂等性控制类型
     * DEFAULT: 基于方法签名
     * PARAM: 基于方法参数
     * SPEL: 基于SpEL表达式提取参数
     * TOKEN: 基于客户端传递的唯一token
     * USER: 基于用户ID
     */
    IdempotentType type() default IdempotentType.DEFAULT;

    /**
     * SpEL表达式,用于从参数中提取幂等标识
     * 仅当 type = SPEL 时生效
     * 例如: "#request.orderId" 或 "#user.id"
     */
    String expression() default "";

    /**
     * 幂等性保持时间(秒)
     * 默认3600秒(1小时)
     * 超过此时间后,相同请求可再次执行
     */
    int expireSeconds() default 3600;

    /**
     * 提示信息
     */
    String message() default "请勿重复提交";

    /**
     * 是否在业务执行完成后删除幂等Key
     * true: 执行成功后立即删除,允许重试
     * false: 保留到过期时间,严格防重
     * 默认false
     */
    boolean deleteAfterProcess() default false;

}
