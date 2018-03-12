package com.entity;

import java.util.Date;

public class WarnRecord {
    private String warnRecordId;

    private Integer warnType;

    private Integer workType;

    private String orderNo;

    private String barCode1;

    private String barCode2;

    private Date warnTime;

    private Date resolveTime;

    public String getWarnRecordId() {
        return warnRecordId;
    }

    public void setWarnRecordId(String warnRecordId) {
        this.warnRecordId = warnRecordId == null ? null : warnRecordId.trim();
    }

    public Integer getWarnType() {
        return warnType;
    }

    public void setWarnType(Integer warnType) {
        this.warnType = warnType;
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

    public String getBarCode1() {
        return barCode1;
    }

    public void setBarCode1(String barCode1) {
        this.barCode1 = barCode1 == null ? null : barCode1.trim();
    }

    public String getBarCode2() {
        return barCode2;
    }

    public void setBarCode2(String barCode2) {
        this.barCode2 = barCode2 == null ? null : barCode2.trim();
    }

    public Date getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
    }

    public Date getResolveTime() {
        return resolveTime;
    }

    public void setResolveTime(Date resolveTime) {
        this.resolveTime = resolveTime;
    }
}