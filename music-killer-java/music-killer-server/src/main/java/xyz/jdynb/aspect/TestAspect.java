package xyz.jdynb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TestAspect {

    @Pointcut("@annotation(xyz.jdynb.annotation.Test)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        log.info("declaringType: {}", signature.getDeclaringType());
        log.info("methodName: {}", signature.getName());
        log.info("modifiers: {}", signature.getModifiers());
        log.info("declaringTypeName: {}", signature.getDeclaringTypeName());
        log.info("shortString: {}", signature.toShortString());

        log.info("=================================");

        log.info("this: {}", joinPoint.getThis());
        log.info("sourceLocation: {}", joinPoint.getSourceLocation());
        log.info("target: {}", joinPoint.getTarget());
        log.info("args: {}", joinPoint.getArgs());
        log.info("kind: {}", joinPoint.getKind());
        log.info("staticPart: {}", joinPoint.getStaticPart());
    }

}
