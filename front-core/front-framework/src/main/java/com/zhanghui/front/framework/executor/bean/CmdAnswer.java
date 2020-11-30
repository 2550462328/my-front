package com.zhanghui.front.framework.executor.bean;

import com.alibaba.fastjson.JSONObject;
import com.zhanghui.front.framework.executor.connector.AnswerAble;


/**
 * 前置回调云方法通用传入参数
 *
 * @author ZhangHui
 * @date 2020/11/13
 */

public class CmdAnswer {
    // 医疗机构编号
    private Integer result;
    // 统筹区
    private String source;
    // 交易时间 yyyyMMdd hh24:mm:ss
    private String tradeNo;
    private Integer priority;
    private String areaCode;
    // json是获取json格式
    private String tradeCode;
    // 交易内容对象
    private String message;
    private String tradeTime;
    private String errCode;
    private JSONObject data;
    private String url;
    private AnswerAble target;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public AnswerAble getTarget() {
        return target;
    }

    public void setTarget(AnswerAble target) {
        this.target = target;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void answer() {
        this.target.answer(this, tradeCode);
    }

    public void answer(String tradeCode) {
        this.target.answer(this, tradeCode);
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

}
