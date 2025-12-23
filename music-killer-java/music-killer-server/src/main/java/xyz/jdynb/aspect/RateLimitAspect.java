package xyz.jdynb.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.jdynb.annotation.RateLimit;
import xyz.jdynb.constant.StatusCodeConstant;
import xyz.jdynb.context.BaseContext;
import xyz.jdynb.enums.RateLimitType;
import xyz.jdynb.exception.BusinessException;
import xyz.jdynb.exception.RateLimitException;
import xyz.jdynb.utils.IPUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 限流切面
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisScript<Long> rateLimitScript;

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";

    @Resource
    private ObjectMapper objectMapper;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(xyz.jdynb.annotation.RateLimit)")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        if (rateLimit == null) {
            return joinPoint.proceed();
        }

        // 构建限流key
        String key = buildKey(rateLimit, signature, joinPoint.getArgs());

        // 执行限流检查
        boolean allowed = checkRateLimit(key, rateLimit.time(), rateLimit.count());

        if (!allowed) {
            log.warn("限流触发: key={}, time={}秒, count={}", key, rateLimit.time(), rateLimit.count());
            throw new RateLimitException(rateLimit.message());
        }

        try {
            // 执行业务方法
            return joinPoint.proceed();
        } catch (Exception e) {
            // 业务执行失败,删除幂等key以允许重试
            if (rateLimit.allowRetry()) {
                deleteKey(key);
                log.debug("业务执行失败,幂等性key已删除: key={}", key);
            }
            throw e;
        }
    }

    private void deleteKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("删除幂等性key失败: key={}", key, e);
        }
    }

    /**
     * 构建限流key
     */
    private String buildKey(RateLimit rateLimit, MethodSignature signature, Object[] args) {
        String key = rateLimit.key();

        // 如果未指定key，使用方法全路径
        if (key == null || key.isEmpty()) {
            key = signature.getDeclaringTypeName() + "." + signature.getName();
        }

        // 根据限流类型添加后缀
        RateLimitType limitType = rateLimit.limitType();
        String suffix = switch (limitType) {
            case IP -> IPUtils.getIpAddress(getRequest());
            case USER -> getUserId();
            case PARAM -> generateParamIdentifier(args);
            case SPEL -> generateSpelIdentifier(rateLimit.expression(), signature, args);
            default -> "default";
        };
        return RATE_LIMIT_KEY_PREFIX + key + ":" + suffix;
    }

    /**
     * 检查是否超过限流阈值
     * 使用 Lua 脚本保证原子性
     */
    private boolean checkRateLimit(String key, int time, int count) {
        try {
            Long result = redisTemplate.execute(
                    rateLimitScript,
                    Collections.singletonList(key),
                    count,
                    time
            );

            return result == 1;
        } catch (Exception e) {
            log.error("限流检查异常: key={}", key, e);
            // 如果Redis异常，默认放行（可根据实际情况调整）
            return true;
        }
    }

    /**
     * 获取用户ID
     * 这里需要根据实际项目的用户认证方式来实现
     * 示例：从请求头或Session中获取
     */
    private String getUserId() {
        try {
            HttpServletRequest request = getRequest();
            if (request == null) {
                throw new RuntimeException("接口异常");
            }

            return BaseContext.getCurrentId().toString();
        } catch (Exception e) {
            log.error("获取用户ID失败", e);
            // 这里可尝试获取用户 id
            throw new BusinessException(StatusCodeConstant.NOT_LOGIN, "获取用户信息失败");
        }
    }

    /**
     * 基于参数生成标识符
     * 将所有参数序列化后进行MD5哈希
     */
    private String generateParamIdentifier(Object[] args) {
        if (args == null || args.length == 0) {
            throw new RuntimeException("接口异常");
        }

        try {
            String paramsJson = objectMapper.writeValueAsString(args);
            return DigestUtils.md5DigestAsHex(paramsJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("参数序列化失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 基于SpEL表达式提取标识符
     * 支持从方法参数中提取特定字段
     */
    private String generateSpelIdentifier(String expression, MethodSignature signature, Object[] args) {
        if (expression == null || expression.isEmpty()) {
            log.warn("SpEL表达式为空,使用默认标识");
            throw new RuntimeException("接口异常");
        }

        try {
            // 构建SpEL上下文
            EvaluationContext context = new StandardEvaluationContext();
            String[] parameterNames = signature.getParameterNames();

            // 将方法参数注册到SpEL上下文
            if (parameterNames != null && args != null) {
                for (int i = 0; i < parameterNames.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }
            }

            // 解析并执行表达式
            Expression exp = parser.parseExpression(expression);
            Object value = exp.getValue(context);

            return value != null ? value.toString() : "spel-null";
        } catch (Exception e) {
            log.error("SpEL表达式解析失败: {}", expression, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前请求
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
