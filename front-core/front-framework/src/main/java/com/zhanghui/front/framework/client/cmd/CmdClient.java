package com.zhanghui.front.framework.client.cmd;

import com.zhanghui.front.exception.ErrorCode;
import com.zhanghui.front.exception.UniformException;
import com.zhanghui.front.framework.client.SendClient;
import com.zhanghui.front.utils.HttpHelper;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 15:18
 * @version：1.0
 */
public class CmdClient implements SendClient {

    private String cmdUrl;
    private static final String HTTP_HEADER = "[{\"key\":\"Content-Type\",\"value\":\"application/json;charset=UTF-8\"}]";

    @Override
    public void configUrl(String url) {
        this.cmdUrl = url;
    }

    @Override
    public String send(String str) {
        String result;
        try {
            result = HttpHelper.httpPost(cmdUrl, str, HTTP_HEADER, 1);
        } catch (Exception e) {
            throw new UniformException(ErrorCode.FRONT_4001, e.getMessage());
        }
        return result;
    }
}
