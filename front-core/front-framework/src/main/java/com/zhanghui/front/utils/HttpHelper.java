package com.zhanghui.front.utils;

import com.alibaba.fastjson.JSONArray;
import com.zhanghui.front.exception.ErrorCode;
import com.zhanghui.front.exception.UniformException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author: ZhangHui
 * @date: 2020/11/13 16:11
 * @version：1.0
 */
@Slf4j
public class HttpHelper {

    public static String httpPost(String v_url, String str, String header,int cycles) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost postreq = new HttpPost(v_url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(300000).setConnectTimeout(300000).build();
        postreq.setConfig(requestConfig);
        JSONArray headerArray = JSONArray.parseArray(header);
        for (int i = 0; i < headerArray.size(); i++) {
            postreq.addHeader(headerArray.getJSONObject(i).getString("key"),
                    headerArray.getJSONObject(i).getString("value"));
        }
        StringEntity requestEntity = null;
        String resultStr = null;
        try {
            requestEntity = new StringEntity(str, ContentType.APPLICATION_JSON);
            postreq.setEntity(requestEntity);
            int i = 0;
            while (i < cycles) {
                response = client.execute(postreq, new BasicHttpContext());
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    resultStr = EntityUtils.toString(entity, "utf-8");
                }
                Integer httpStatus = response.getStatusLine().getStatusCode();
                if (httpStatus != 200) {
                    log.error(resultStr);
                    String errMessage = "请求url=" + v_url + ",请求失败,http返回code:"
                            + response.getStatusLine().getStatusCode();
                    log.error(errMessage);
                    i++;
                    if (i == cycles) {
                        String outMessage = "请求失败,http返回code:" + httpStatus;
                        if (httpStatus.toString().indexOf("4") == 0) {
                            throw new UniformException(ErrorCode.FRONT_4001, outMessage);
                        } else if (httpStatus.toString().indexOf("5") == 0) {
                            throw new UniformException(ErrorCode.JB_5001, outMessage);
                        } else {
                            throw new UniformException(ErrorCode.JB_5002, outMessage);
                        }
                    }
                } else {
                    break;
                }
            }
            return resultStr;
        } catch (UniformException e1) {
            log.error(e1.getMessage());
            throw e1;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UniformException(e.getMessage());
        } finally {
            if (response != null)
                response.close();
        }
    }

    public static String httpPostForSoap(String v_url, String str, String tradeCode) throws Exception {
        log.info(String.format("[T-%s-HIS-IN][%s]", tradeCode, str));

        // Response err_response = new Response();
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost postreq = new HttpPost(v_url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
        postreq.setConfig(requestConfig);
        postreq.setHeader("Content-Type", "text/xml; charset=utf-8");

        if (tradeCode.equals("4004"))
            postreq.setHeader("SOAPAction", "http://tempuri.org/IAnJiRMYY/GetReportList");
        if (tradeCode.equals("4005"))
            postreq.setHeader("SOAPAction", "http://tempuri.org/IAnJiRMYY/GetReportDetail");

        StringEntity requestEntity = null;
        try {
            requestEntity = new StringEntity(str, "utf-8");
            postreq.setEntity(requestEntity);
            response = client.execute(postreq, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception(
                        "request url failed, http code=" + response.getStatusLine().getStatusCode() + ", url=" + v_url);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                log.info(String.format("[T-%s-HIS-OUT][%s]", tradeCode, resultStr.replaceAll("&quot;", "\"")));

                return resultStr;
            }
        } catch (IOException e) {
            String errMessage = "请求url=" + v_url + ", 返回异常编号=" + e.getMessage();
            System.out.println(errMessage);
            throw e;
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("异常");
                    throw e;
                }
        }
        return null;
    }
}