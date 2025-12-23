package xyz.jdynb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import xyz.jdynb.annotation.AutoFill;
import xyz.jdynb.constant.AutoFillConstant;
import xyz.jdynb.enums.OperationType;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* xyz.jdynb.mapper.*.*(..)) && @annotation(xyz.jdynb.annotation.AutoFill))")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);

        OperationType operationType = autoFill.value();

        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];

        log.info("开始自动填充字段: {}", entity);

        LocalDateTime now = LocalDateTime.now();

        try {
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME);
            setUpdateTime.invoke(entity, now);
            switch (operationType) {
                case UPDATE:
                    break;

                case INSERT:
                    Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME);
                    setCreateTime.invoke(entity, now);
                    break;
            }

        } catch (Exception e) {
            log.error("自动填充失败: {}", e.getMessage());
        }
    }
}
