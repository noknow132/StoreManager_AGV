package com.entity;

public class WarnType {
    private String warnTypeId;

    private Integer warnCode;

    private String warnName;

    private String resolve;

    public String getWarnTypeId() {
        return warnTypeId;
    }

    public void setWarnTypeId(String warnTypeId) {
        this.warnTypeId = warnTypeId == null ? null : warnTypeId.trim();
    }

    public Integer getWarnCode() {
        return warnCode;
    }

    public void setWarnCode(Integer warnCode) {
        this.warnCode = warnCode;
    }

    public String getWarnName() {
        return warnName;
    }

    public void setWarnName(String warnName) {
        this.warnName = warnName == null ? null : warnName.trim();
    }

    public String getResolve() {
        return resolve;
    }

    public void setResolve(String resolve) {
        this.resolve = resolve == null ? null : resolve.trim();
    }
}