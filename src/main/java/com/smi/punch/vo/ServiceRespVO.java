package com.smi.punch.vo;

import java.util.List;

public class ServiceRespVO {
	private String RETCODE;
	private Object RET;
	private List<String> MSG;

	public String getRETCODE() {
		return RETCODE;
	}

	public void setRETCODE(String rETCODE) {
		RETCODE = rETCODE;
	}

	public Object getRET() {
		return RET;
	}

	public void setRET(Object rET) {
		RET = rET;
	}

	public List<String> getMSG() {
		return MSG;
	}

	public void setMSG(List<String> mSG) {
		MSG = mSG;
	}

	@Override
	public String toString() {
		return "ServiceRespVO [RETCODE=" + RETCODE + ", RET=" + RET + ", MSG=" + MSG + "]";
	}
	
}
