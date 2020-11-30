package com.zhanghui.front.framework.executor.handler.mapping;

/**
 * @author: ZhangHui
 * @date: 2020/11/20 20:29
 * @version：1.0
 */
public interface HandlerMapping {

    BusinessHandlerBean getHandler(String cmdType);
}
