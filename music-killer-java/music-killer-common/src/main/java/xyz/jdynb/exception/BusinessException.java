package xyz.jdynb.exception;

import lombok.Getter;
import xyz.jdynb.constant.StatusCodeConstant;

public class BusinessException extends RuntimeException {

    @Getter
    private Integer code;

    public BusinessException() {}

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = StatusCodeConstant.ERROR;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
