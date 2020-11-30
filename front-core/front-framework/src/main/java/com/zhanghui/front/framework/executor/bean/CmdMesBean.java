package com.zhanghui.front.framework.executor.bean;

import com.alibaba.fastjson.JSONObject;
import com.zhanghui.front.framework.executor.connector.AnswerAble;

/**
 * Created by caok on 2015/11/25. 前置机接收指令对象
 */
public class CmdMesBean implements Comparable<CmdMesBean> {

    /**
     * 指令id用于排序
     */
    private String tradeNo;
    // 指令操作内容 1:调用医保接口 2:调用医院接口 3:其他
    private Integer priority;
    private String tradeCode;// 调用方法
    private String expireTime;// 医疗机构编号
    private String areaCode;
    private AnswerAble target;
    private JSONObject data;// 当入参protocol为json时，该变量会有相应数据以json格式获取
    private String url;
    public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}


	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
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

    @Override
	public int compareTo(CmdMesBean jobmessage) {
        if (this == jobmessage)
            return 0;
        if (jobmessage == null)
            return 0;
        return 0;
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
