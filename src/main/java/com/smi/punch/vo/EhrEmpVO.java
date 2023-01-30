package com.smi.punch.vo;

import com.google.gson.annotations.SerializedName;

public class EhrEmpVO {
	@SerializedName("compID")
	private String compID;
	@SerializedName("empNo")
	private String empNo;
	@SerializedName("cname")
	private String cname;
	@SerializedName("ename")
	private String ename;
	@SerializedName("email")
	private String email;
	@SerializedName("depID")
	private String depID;
	@SerializedName("depName")
	private String depName;
	@SerializedName("worklocation")
	private String worklocation;
	@SerializedName("hireDate")
	private String hireDate;

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepID() {
		return depID;
	}

	public void setDepID(String depID) {
		this.depID = depID;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getWorklocation() {
		return worklocation;
	}

	public void setWorklocation(String worklocation) {
		this.worklocation = worklocation;
	}

	public String getCompID() {
		return compID;
	}

	public void setCompID(String compID) {
		this.compID = compID;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EhrEmpVO) {
			return ((EhrEmpVO) obj).getEmpNo().contentEquals(this.getEmpNo());
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "EhrEmpVO [compID=" + compID + ",depName="+depName+", empNo=" + empNo + ", cname=" + cname + "]";
	}

}
