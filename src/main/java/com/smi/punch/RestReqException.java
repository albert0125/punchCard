package com.smi.punch;

import java.util.List;

public class RestReqException extends Exception{
	private List<String> msg;
	public RestReqException(List<String> msg) {
		this.msg = msg;
	}
	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer();
		if(null!=super.getMessage()) {
			sb.append(super.getMessage()+"\r\n");
		}
		
		for(String s : msg) {
			sb.append(s+"\r\n");
		}
		return sb.toString();
	}
	
}
