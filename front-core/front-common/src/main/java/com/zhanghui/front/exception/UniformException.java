package com.zhanghui.front.exception;

/**
 * Created by wxt on 2017/12/25.
 */
public class UniformException extends RuntimeException {

	private String code;

	public UniformException(ErrorCode errorCode){
		super(errorCode.getMsg());
		this.code = errorCode.getCode();
	}
	public UniformException(ErrorCode errorCode,String message){
		super(errorCode.getMsg()+"_"+message);
		this.code = errorCode.getCode();
	}

    public UniformException(String message){
        super(message);
    }

    public String getCode(){
    	return this.code;
    }
}
