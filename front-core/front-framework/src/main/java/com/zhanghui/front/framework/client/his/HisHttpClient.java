package com.zhanghui.front.framework.client.his;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhanghui.front.framework.context.ContextUtil;
import com.zhanghui.front.exception.ErrorCode;
import com.zhanghui.front.exception.UniformException;
import com.zhanghui.front.utils.StringUtils;
import com.zhanghui.front.framework.client.SendClient;
import com.zhanghui.front.utils.HttpHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 16:18
 * @version：1.0
 */
@Slf4j
public class HisHttpClient implements SendClient {

    private String hostUrl;

    private String header = "[{\"key\":\"Content-Type\",\"value\":\"application/json;charset=UTF-8\"}]";

    @Override
    public void configUrl(String url) {
        this.hostUrl = url;
    }

    @Override
    public String send(String str) {
        String result;
        try {
            JSONObject requestJson = JSON.parseObject(str);

            String url = requestJson.getString("url");
            if (StringUtils.isBlank(url)) {
                throw new UniformException(ErrorCode.FRONT_4001, "社保云未配置调用地址");
            }
            result = HttpHelper.httpPost(url, str, header, 1);
            if (StringUtils.isBlank(result)) {
                throw new UniformException(ErrorCode.JB_5001, "返回数据格式有误");
            }
            JSONObject json = JSON.parseObject(result);
            if (json == null) {
                throw new UniformException(ErrorCode.JB_5001, "返回数据格式有误");
            }
            if (json.getInteger("result") == null) {
                throw new UniformException(ErrorCode.JB_5001, "返回数据格式有误");
            }
            Map map = new HashMap<>();
            if (json.getInteger("result") == 1 || json.getInteger("result") == 0) {
                map.put("result", 0);
            } else {
                map.put("result", -1);
                if (StringUtils.isBlank(json.getString("errCode"))) {
                    map.put("errCode", ErrorCode.JB_5001.getCode());
                } else {
                    map.put("errCode", json.getString("errCode"));
                }
                map.put("message", json.getString("message"));

            }
            map.put("data", json.get("data"));
            map.put("areaCode", ContextUtil.getProperty("areaCode"));
            map.put("appId", ContextUtil.getProperty("appId"));
            map.put("source", "");
            map.put("tradeNo", json.get("tradeNo"));
            map.put("tradeTime", json.get("tradeTime"));
            result = JSON.toJSONString(map);
        } catch (Exception e) {
            log.error("请求经办系统失败," + e.getMessage());
            throw new UniformException(ErrorCode.FRONT_4004, e.getMessage());
        }
        return result;
    }
}
