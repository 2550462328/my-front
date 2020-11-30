package com.zhanghui.front.framework.executor.handler.mapping;

import com.zhanghui.front.framework.executor.handler.Handler;

import java.util.List;

/**
 * @author: ZhangHui
 * @date: 2020/11/20 20:29
 * @version：1.0
 */
public class BusinessHandlerBean {

    private String information;
    private String principle;
    private List<Handler> handlers;

    public BusinessHandlerBean(String information, String principle, List<Handler> handlers) {
        this.information = information;
        this.principle = principle;
        this.handlers = handlers;
    }

    public String getInformation() {
        return information;
    }

    public List<Handler> getHandlers() {
        return handlers;
    }

    public String getPrinciple() {
        return principle;
    }
}
