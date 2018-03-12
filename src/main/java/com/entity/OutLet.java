package com.entity;

import java.util.Date;

public class OutLet {
    private String outletId;

    private String outletName;

    private Integer outNo;

    private Integer outType;

    private Integer isUesd;

    private Date createTime;
    
    private Date updateTime;

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId == null ? null : outletId.trim();
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName == null ? null : outletName.trim();
    }

    public Integer getOutNo() {
        return outNo;
    }

    public void setOutNo(Integer outNo) {
        this.outNo = outNo;
    }

    public Integer getOutType() {
        return outType;
    }

    public void setOutType(Integer outType) {
        this.outType = outType;
    }

    public Integer getIsUesd() {
        return isUesd;
    }

    public void setIsUesd(Integer isUesd) {
        this.isUesd = isUesd;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}