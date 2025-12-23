package xyz.jdynb.exception;

/**
 * 幂等性校验异常
 */
public class IdempotentException extends RuntimeException {

    public IdempotentException() {
    }

    public IdempotentException(String msg) {
        super(msg);
    }

}
