package com.zhanghui.front.framework.executor.handler.mapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhanghui.front.framework.annotation.BeanIfMissing;
import com.zhanghui.front.framework.context.Container;
import com.zhanghui.front.framework.context.inject.BeanInjector;
import com.zhanghui.front.framework.context.inject.Injector;
import com.zhanghui.front.framework.context.inject.ValueInjector;
import com.zhanghui.front.framework.executor.handler.Handler;
import com.zhanghui.front.framework.parsing.DefaultPropertyResolver;
import com.zhanghui.front.framework.parsing.PropertyResolver;

import java.util.*;

/**
 * @author: ZhangHui
 * @date: 2020/11/20 20:30
 * @version：1.0
 */
@BeanIfMissing
public class DefaultHandlerMapping implements HandlerMapping, Container {

    private Map<String, BusinessHandlerBean> handlerBeanMap = new HashMap<>();

    private static final String DEFAULT_HANDLER_MAPPING = "default";

    private static final String CMD_HANDLER_CONFIG_NAME = System.getProperty("user.home") + "/webapp-conf/cmdConfig.properties";

    @Override
    public void init() {
        PropertyResolver propertyResolver = new DefaultPropertyResolver();
        Properties properties = new Properties();
        propertyResolver.resolve(properties, CMD_HANDLER_CONFIG_NAME);

        Enumeration<String> enumeration = (Enumeration<String>) properties.propertyNames();
        Injector beanInjector = new BeanInjector();
        Injector valueInjector = new ValueInjector();

        while (enumeration.hasMoreElements()) {
            String propertyName = enumeration.nextElement();

            if (propertyName.contains("handler.")) {
                JSONObject jsonObject = JSON.parseObject(properties.getProperty(propertyName));
                List<Handler> list = new ArrayList<>();
                try {
                    JSONArray jsonArray = (JSONArray) jsonObject.get("handlers");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Handler handler = (Handler) Class.forName(jsonArray.getJSONObject(i).getString("name"))
                                .newInstance();
                        if (jsonArray.getJSONObject(i).getString("exception") != null) {
                            Handler exceptionHandler = (Handler) Class
                                    .forName(jsonArray.getJSONObject(i).getString("exception")).newInstance();
                            beanInjector.inject(exceptionHandler);
                            valueInjector.inject(exceptionHandler);
                            handler.setExceptionHandler(exceptionHandler);
                        }
                        valueInjector.inject(handler);
                        beanInjector.inject(handler);
                        list.add(handler);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BusinessHandlerBean bean = new BusinessHandlerBean(jsonObject.getString("cmdType"),
                        jsonObject.getString("principle"), list);
                String[] cmdTypes = bean.getInformation().split(",");
                for (String cmdType : cmdTypes) {
                    handlerBeanMap.put(cmdType, bean);
                }
            }
        }

    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public BusinessHandlerBean getHandler(String cmdType) {
        if (handlerBeanMap.get(cmdType) != null) {
            return handlerBeanMap.get(cmdType);
        } else {
            return handlerBeanMap.get(DEFAULT_HANDLER_MAPPING);
        }
    }
}
