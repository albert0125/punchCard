package com.smi.punch.vo;

public class DoorLogVO {
	private String pk;
	private String cardNo;
	private String entry;
	private String device;
	private int inOrOut;
	private long time;
	private long recvTime;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public int getInOrOut() {
		return inOrOut;
	}

	public void setInOrOut(int inOrOut) {
		this.inOrOut = inOrOut;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(long recvTime) {
		this.recvTime = recvTime;
	}



}
