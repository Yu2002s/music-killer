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
import xyz.jdynb.annotation.Idempotent;
import xyz.jdynb.context.BaseContext;
import xyz.jdynb.enums.IdempotentType;
import xyz.jdynb.exception.IdempotentException;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 幂等性切面
 * 高性能实现: 基于Redis + Lua脚本保证原子性操作
 */
@Slf4j
@Aspect
@Component
public class IdempotentAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "idempotentScript")
    private RedisScript<Long> idempotentScript;

    private static final String IDEMPOTENT_KEY_PREFIX = "idempotent:";
    private static final String IDEMPOTENT_TOKEN_HEADER = "X-Idempotent-Token";

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 环绕通知: 执行前检查幂等性,执行后可选择删除key
     */
    @Around("@annotation(xyz.jdynb.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        if (idempotent == null) {
            return joinPoint.proceed();
        }

        // 构建幂等key
        String idempotentKey = buildIdempotentKey(idempotent, signature, joinPoint.getArgs());
        log.debug("幂等性检查: key={}", idempotentKey);

        // 执行幂等性检查(Redis + Lua原子操作)
        boolean allowed = checkIdempotent(idempotentKey, idempotent.expireSeconds());

        if (!allowed) {
            log.warn("幂等性校验失败,重复请求: key={}", idempotentKey);
            throw new IdempotentException(idempotent.message());
        }

        try {
            // 执行业务方法
            Object result = joinPoint.proceed();

            // 如果配置了执行后删除,则删除幂等key(允许重试)
            if (idempotent.deleteAfterProcess()) {
                deleteIdempotentKey(idempotentKey);
                log.debug("幂等性key已删除(允许重试): key={}", idempotentKey);
            }

            return result;
        } catch (Exception e) {
            // 业务执行失败,删除幂等key以允许重试
            if (idempotent.deleteAfterProcess()) {
                deleteIdempotentKey(idempotentKey);
                log.debug("业务执行失败,幂等性key已删除: key={}", idempotentKey);
            }
            throw e;
        }
    }

    /**
     * 构建幂等性key
     * 根据不同策略生成唯一标识
     */
    private String buildIdempotentKey(Idempotent idempotent, MethodSignature signature, Object[] args) {
        String baseKey = idempotent.key();

        // 如果未指定key,使用方法全路径作为基础key
        if (baseKey == null || baseKey.isEmpty()) {
            baseKey = signature.getDeclaringTypeName() + "." + signature.getName();
        }

        // 根据类型生成唯一标识
        String identifier = generateIdentifier(idempotent, signature, args);

        // 拼接完整key
        return IDEMPOTENT_KEY_PREFIX + baseKey + ":" + identifier;
    }

    /**
     * 根据不同策略生成唯一标识符
     */
    private String generateIdentifier(Idempotent idempotent, MethodSignature signature, Object[] args) {
        IdempotentType type = idempotent.type();

        return switch (type) {
            case DEFAULT -> "default";
            case PARAM -> generateParamIdentifier(args);
            case SPEL -> generateSpelIdentifier(idempotent.expression(), signature, args);
            case TOKEN -> generateTokenIdentifier();
            case USER -> generateUserIdentifier();
        };
    }

    /**
     * 基于参数生成标识符
     * 将所有参数序列化后进行MD5哈希
     */
    private String generateParamIdentifier(Object[] args) {
        if (args == null || args.length == 0) {
            return "no-param";
        }

        try {
            String paramsJson = objectMapper.writeValueAsString(args);
            return DigestUtils.md5DigestAsHex(paramsJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("参数序列化失败", e);
            return "param-error";
        }
    }

    /**
     * 基于SpEL表达式提取标识符
     * 支持从方法参数中提取特定字段
     */
    private String generateSpelIdentifier(String expression, MethodSignature signature, Object[] args) {
        if (expression == null || expression.isEmpty()) {
            log.warn("SpEL表达式为空,使用默认标识");
            return "spel-empty";
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
            return "spel-error";
        }
    }

    /**
     * 基于客户端Token生成标识符
     * 从请求头获取 X-Idempotent-Token
     */
    private String generateTokenIdentifier() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            throw new IdempotentException("无法获取请求上下文");
        }

        String token = request.getHeader(IDEMPOTENT_TOKEN_HEADER);
        if (token == null || token.isEmpty()) {
            throw new IdempotentException("缺少幂等性Token,请在请求头中添加: " + IDEMPOTENT_TOKEN_HEADER);
        }

        return token;
    }

    /**
     * 基于用户ID生成标识符
     * 从ThreadLocal上下文获取当前用户ID
     */
    private String generateUserIdentifier() {
        try {
            Long userId = BaseContext.getCurrentId();
            if (userId != null) {
                return "user-" + userId;
            }

            // 如果ThreadLocal中没有,尝试从请求头获取
            HttpServletRequest request = getRequest();
            if (request != null) {
                String userIdHeader = request.getHeader("User-Id");
                if (userIdHeader != null && !userIdHeader.isEmpty()) {
                    return "user-" + userIdHeader;
                }
            }

            throw new IdempotentException("无法获取用户ID,请先登录");
        } catch (Exception e) {
            log.error("获取用户ID失败", e);
            throw new IdempotentException("用户身份验证失败");
        }
    }

    /**
     * 检查幂等性
     * 使用Lua脚本保证原子性: SET NX EX
     */
    private boolean checkIdempotent(String key, int expireSeconds) {
        try {
            Long result = redisTemplate.execute(
                idempotentScript,
                Collections.singletonList(key),
                expireSeconds
            );

            // 返回1表示设置成功(首次请求),返回0表示key已存在(重复请求)
            return result != null && result == 1;
        } catch (Exception e) {
            log.error("幂等性检查异常: key={}", key, e);
            // Redis异常时的降级策略: 默认拒绝执行(保守策略)
            // 可根据业务需求调整为放行
            return false;
        }
    }

    /**
     * 删除幂等性key
     */
    private void deleteIdempotentKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("删除幂等性key失败: key={}", key, e);
        }
    }

    /**
     * 获取当前HTTP请求
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
