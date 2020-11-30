package com.zhanghui.front.framework.executor.handler;

/**
 * @author: ZhangHui
 * @date: 2020/11/21 14:02
 * @version：1.0
 */
public abstract class AbstractHandler implements Handler {
    Handler exceptionHandler;

    @Override
    public void setExceptionHandler(Handler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public Handler getExceptionHandler() {
        return this.exceptionHandler;
    }
}
