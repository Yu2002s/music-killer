package xyz.jdynb.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.jdynb.constant.MessageConstant;
import xyz.jdynb.exception.BusinessException;
import xyz.jdynb.exception.IdempotentException;
import xyz.jdynb.exception.RateLimitException;
import xyz.jdynb.result.Result;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获系统异常
     *
     * @param ex 错误异常
     * @return 响应结果
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(Exception ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error("系统异常");
    }

    @ExceptionHandler
    public Result<Void> exceptionHandler(BusinessException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理sql异常
     *
     * @param ex 错误异常
     * @return 响应结果
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            // 如果是数据库唯一值是已存在的情况处理错误
            String[] split = message.split(" ");
            String param = split[2];
            return Result.error(param + MessageConstant.ALREADY_EXISTS);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

    /**
     * 处理限流异常
     */
    @ExceptionHandler(RateLimitException.class)
    public Result<Void> handleRateLimitException(RateLimitException e) {
        log.warn("触发限流: {}", e.getMessage());
        return Result.error(MessageConstant.TOO_MONEY_REQUEST);
    }

    /**
     * 处理幂等性校验异常
     */
    @ExceptionHandler(IdempotentException.class)
    public Result<Void> handleIdempotentException(IdempotentException e) {
        log.warn("幂等性校验失败: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 方法参数校验失败异常处理
     *
     * @param e       异常对象
     * @param request 请求对象
     * @return result
     */
    @ExceptionHandler
    public Result<String> exceptionValidException(BindException e, HttpServletRequest request) {
        FieldError fieldError = e.getFieldError();
        if (fieldError == null) {
            return Result.error("请求参数错误");
        }
        log.error("{} 请求参数错误, {}:{} {}", request.getRequestURI(), fieldError.getField(),
                fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        return Result.error(fieldError.getDefaultMessage());
    }

    /**
     * 方法参数校验
     * @param e 异常
     * @param request 请求
     * @return 响应内容
     */
    @ExceptionHandler
    public Result<String> exceptionValidException(ConstraintViolationException e, HttpServletRequest request) {
        String error = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.error("{} 请求参数错误, {}", request.getRequestURI(), error);
        return Result.error(error);
    }

}
