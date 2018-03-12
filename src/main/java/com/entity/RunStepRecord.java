package com.entity;

import java.util.Date;

public class RunStepRecord {
    private String runRecordId;

    private Date runTime;

    private Integer workType;

    private String workId;

    private String orderId;

    private String barCode;

    private String runStepId;

    public String getRunRecordId() {
        return runRecordId;
    }

    public void setRunRecordId(String runRecordId) {
        this.runRecordId = runRecordId == null ? null : runRecordId.trim();
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
    }

    public String getRunStepId() {
        return runStepId;
    }

    public void setRunStepId(String runStepId) {
        this.runStepId = runStepId == null ? null : runStepId.trim();
    }
}