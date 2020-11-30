package com.zhanghui.front.framework.exception;

/**
 * @author ZhangHui
 * @date 2020/11/10
 */
public class InjectException extends RuntimeException {
    public InjectException() {
    }

    public InjectException(String message) {
        super(message);
    }

    public InjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public InjectException(Throwable cause) {
        super(cause);
    }

    public InjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
