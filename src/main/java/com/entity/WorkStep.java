package com.entity;

import java.util.Date;

public class WorkStep {
	private String workId;

	private String orderId;

	private Integer workType;

	private String orderNo;

	private Integer workStatue;

	private String fringeCode;

	private Integer count;

	private String getPlace;

	private String putPlace;

	private Date insertTime;
	
    private Date scanTime;

	private Date inputStoreTime;

	private Date outputStoreTime;

	private String rebotCode;

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId == null ? null : workId.trim();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public Integer getWorkType() {
		return workType;
	}

	public void setWorkType(Integer workType) {
		this.workType = workType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public Integer getWorkStatue() {
		return workStatue;
	}

	public void setWorkStatue(Integer workStatue) {
		this.workStatue = workStatue;
	}

	public String getFringeCode() {
		return fringeCode;
	}

	public void setFringeCode(String fringeCode) {
		this.fringeCode = fringeCode == null ? null : fringeCode.trim();
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getGetPlace() {
		return getPlace;
	}

	public void setGetPlace(String getPlace) {
		this.getPlace = getPlace == null ? null : getPlace.trim();
	}

	public String getPutPlace() {
		return putPlace;
	}

	public void setPutPlace(String putPlace) {
		this.putPlace = putPlace == null ? null : putPlace.trim();
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getInputStoreTime() {
		return inputStoreTime;
	}

	public void setInputStoreTime(Date inputStoreTime) {
		this.inputStoreTime = inputStoreTime;
	}

	public Date getOutputStoreTime() {
		return outputStoreTime;
	}

	public void setOutputStoreTime(Date outputStoreTime) {
		this.outputStoreTime = outputStoreTime;
	}

	public String getRebotCode() {
		return rebotCode;
	}

	public void setRebotCode(String rebotCode) {
		this.rebotCode = rebotCode == null ? null : rebotCode.trim();
	}

	public Date getScanTime() {
		return scanTime;
	}

	public void setScanTime(Date scanTime) {
		this.scanTime = scanTime;
	}
}