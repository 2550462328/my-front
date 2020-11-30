package com.zhanghui.front.framework.client;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 10:10
 * @version：1.0
 */
public interface SendClient {

    void configUrl(String url);

    String send(String str);
}
