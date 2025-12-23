package xyz.jdynb.enums;

/**
 * 限流类型枚举
 */
public enum RateLimitType {

    /**
     * 默认策略：根据请求方法全路径限流
     */
    DEFAULT,

    /**
     * 根据IP地址限流
     */
    IP,

    /**
     * 根据参数进行限流
     */
    PARAM,

    /**
     * 根据用户ID限流（需要从请求中获取用户信息）
     */
    USER,
    /**
     * 基于SpEL表达式
     * 从方法参数中通过SpEL表达式提取特定字段作为幂等标识
     * 适用于需要精确控制幂等字段的场景
     * 例如: #request.orderId, #dto.userId
     */
    SPEL,
}
