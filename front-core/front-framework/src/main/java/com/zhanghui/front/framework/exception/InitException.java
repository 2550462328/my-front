package com.zhanghui.front.framework.exception;

/**
 * @author ZhangHui
 * @date 2020/11/10
 */
public class InitException extends RuntimeException {
    public InitException() {
    }

    public InitException(String message) {
        super(message);
    }

    public InitException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitException(Throwable cause) {
        super(cause);
    }

    public InitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
