package xyz.jdynb.constant;

/**
 * 响应信息常量
 */
public class MessageConstant {

    public static final String UNKNOWN_ERROR = "未知错误";

    public static final String ALREADY_EXISTS = "";

    public static final String TOO_MONEY_REQUEST = "请求频繁，请稍后重试";

    // 幂等性相关
    public static final String IDEMPOTENT_DUPLICATE_REQUEST = "请勿重复提交";
    public static final String IDEMPOTENT_TOKEN_MISSING = "缺少幂等性Token";
    public static final String IDEMPOTENT_USER_NOT_LOGIN = "请先登录后再操作";
}
