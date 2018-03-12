package com.entity;

import java.util.Date;

public class ChangeStore {
	private String changeId;

	private String changeNo;

	private String barCode;

	private String storeIdFrom;

	private String storeIdIng;

	private String storeIdTo;

	private Date changeTime;

	private Integer statue;

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId == null ? null : changeId.trim();
	}

	public String getChangeNo() {
		return changeNo;
	}

	public void setChangeNo(String changeNo) {
		this.changeNo = changeNo == null ? null : changeNo.trim();
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode == null ? null : barCode.trim();
	}

	public String getStoreIdFrom() {
		return storeIdFrom;
	}

	public void setStoreIdFrom(String storeIdFrom) {
		this.storeIdFrom = storeIdFrom == null ? null : storeIdFrom.trim();
	}

	public String getStoreIdIng() {
		return storeIdIng;
	}

	public void setStoreIdIng(String storeIdIng) {
		this.storeIdIng = storeIdIng == null ? null : storeIdIng.trim();
	}

	public String getStoreIdTo() {
		return storeIdTo;
	}

	public void setStoreIdTo(String storeIdTo) {
		this.storeIdTo = storeIdTo == null ? null : storeIdTo.trim();
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Integer getStatue() {
		return statue;
	}

	public void setStatue(Integer statue) {
		this.statue = statue;
	}
}