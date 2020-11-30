package com.zhanghui.front.exception;

/**
 * Created by wxt on 2017/12/25.
 */
public enum ErrorCode {
    JB_5001("5001","经办系统处理失败"),
    JB_5002("5002","经办系统异常_网络异常"),
    FRONT_4001("4001","前置系统处理失败"),
    FRONT_4004("4004","前置服务异常_未知代码bug"),
    FRONT_9999("9999","前置调用社保云失败")
    ;
    private final String code;
    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
