package xyz.jdynb.enums;

/**
 * 幂等性类型枚举
 */
public enum IdempotentType {

    /**
     * 默认策略 - 基于方法签名
     * 适用于无参数或参数不影响幂等性的场景
     */
    DEFAULT,

    /**
     * 基于方法参数
     * 将方法所有参数序列化后作为幂等标识
     * 适用于参数作为唯一标识的场景
     */
    PARAM,

    /**
     * 基于SpEL表达式
     * 从方法参数中通过SpEL表达式提取特定字段作为幂等标识
     * 适用于需要精确控制幂等字段的场景
     * 例如: #request.orderId, #dto.userId
     */
    SPEL,

    /**
     * 基于Token令牌
     * 客户端需在请求头中传递唯一token(如 X-Idempotent-Token)
     * 适用于前端生成唯一标识的场景
     */
    TOKEN,

    /**
     * 基于用户ID
     * 同一用户对相同接口的重复调用进行防重
     * 适用于用户维度的幂等控制
     */
    USER,
}
