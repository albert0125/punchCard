package com.smi.punch.vo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ReqResultVO {
	@SerializedName("RETCODE")
	private String retCode;
	@SerializedName("RET")
	private Object ret;
	@SerializedName("MSG")
	private List<String> msg;
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public Object getRet() {
		return ret;
	}
	public void setRet(Object ret) {
		this.ret = ret;
	}
	public List<String> getMsg() {
		return msg;
	}
	public void setMsg(List<String> msg) {
		this.msg = msg;
	}
	
	
}
