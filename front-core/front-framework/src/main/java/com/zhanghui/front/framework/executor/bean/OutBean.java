package com.zhanghui.front.framework.executor.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 公共传出报文
 */
public class OutBean {

    private String result;
    private String tradeCode;
    private String hospitalCode;
    private String tradeTime;
    private String message;
    private String errCode;
    private String url;
    private JSONObject data;
    private String areaCode;
    public OutBean() {
        this.data = new JSONObject();
    }

    public OutBean(String tradeCode, String hospitalCode) {
        this.tradeCode = tradeCode;
        this.hospitalCode = hospitalCode;
        this.tradeTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        this.data = new JSONObject();
    }
    
    public OutBean(String tradeCode, String areaCode,String url) {
        this.tradeCode = tradeCode;
        this.areaCode = areaCode;
        this.url = url;
        this.tradeTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        this.data = new JSONObject();
    }
    
    public void busError(String errMsg, JSONObject data) {
        this.message = errMsg;
        this.data = data;
        this.result = "-1";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    
    public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


    
}
