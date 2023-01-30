package com.smi.punch.vo;

public class PunchRecordVO {
	private int id;
	private String deptNo;
	private String deptName;
	private String empNo;
	private String name;
	private long punchDateTime;
	private String punchType;
	private String punchTypeName;
	private long createDateTime;
	private String otherDesc;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPunchDateTime() {
		return punchDateTime;
	}

	public void setPunchDateTime(long punchDateTime) {
		this.punchDateTime = punchDateTime;
	}

	public String getPunchType() {
		return punchType;
	}

	public void setPunchType(String punchType) {
		this.punchType = punchType;
	}

	public long getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(long createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOtherDesc() {
		return otherDesc;
	}

	public void setOtherDesc(String otherDesc) {
		this.otherDesc = otherDesc;
	}
	
	public String getPunchTypeName() {
		return punchTypeName;
	}

	public void setPunchTypeName(String punchTypeName) {
		this.punchTypeName = punchTypeName;
	}

	@Override
	public String toString() {
		return "PunchRecordVO [id=" + id + ", deptNo=" + deptNo + ", empNo=" + empNo + ", name=" + name
				+ ", punchDateTime=" + punchDateTime + ", punchType=" + punchType + ", status=" + status + "]";
	}

}
