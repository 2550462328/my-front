package com.zhanghui.front.framework.boot;

import com.zhanghui.front.framework.context.ContextBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Properties;

/**
 * 启动类
 * @author: ZhangHui
 * @date: 2020/11/10 14:27
 * @version：1.0
 */
public class FrontBootstrap {

    public static final String CONFIG_LOCATIONS = "config.locations";
    public static final String CONFIG_LOCATION = "config.location";
    public static final String PACKAGE_SCAN_LOCATION = "package.scan.location";
    public static final String CURRENT_PACKAGE_LOCATION = "com.zhanghui.front.framework";
    public static final String HIS_CLIENT_CLASS = "his.client.class";
    public static final String CMD_CLIENT_CLASS = "cmd.client.class";
    public static final String CLIENT_HOST_URL = "host.url";
    public static final String CLIENT_CMD_URL = "cmd.url";

    public static final String CONNECTOR_STATUS_OPEN = "on";

    public static final String SOURCE_BEAN_NAME = "source";

    public static final String PROPERTIES_VALUE_DIT = "=";

    public static void start(Object source, List<String> pathList, String... propertiesValues){
        Properties properties = new Properties();

        StringBuilder contextPaths = new StringBuilder();

        for(String path : pathList){
            if(contextPaths.length() == 0){
                contextPaths.append(path);
            }else{
                contextPaths.append(",").append(path);
            }
        }

        properties.setProperty(CONFIG_LOCATIONS,contextPaths.toString());
        setProperties(properties, propertiesValues);
        ContextBuilder contextBuilder = new ContextBuilder(source, properties);
        contextBuilder.builder().start();
    }

    private static void setProperties(Properties properties, String[] propertiesValues) {
        for(String propertiesValue : propertiesValues){
            String[] valueSplit = propertiesValue.split(PROPERTIES_VALUE_DIT);
            if(valueSplit.length < 2 || StringUtils.isBlank(valueSplit[0]) || StringUtils.isBlank(valueSplit[1])){
                continue;
            }
            properties.setProperty(valueSplit[0], valueSplit[1]);
        }
    }
}
