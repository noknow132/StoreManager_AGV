package com.entity;

public class PlcConnConfig {
    private String picId;

    private String plcName;

    private String localIp;

    private String plcIp;

    private Integer plcPort;

    private Integer refresh;

    private Integer plcType;

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId == null ? null : picId.trim();
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName == null ? null : plcName.trim();
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp == null ? null : localIp.trim();
    }

    public String getPlcIp() {
        return plcIp;
    }

    public void setPlcIp(String plcIp) {
        this.plcIp = plcIp == null ? null : plcIp.trim();
    }

    public Integer getPlcPort() {
        return plcPort;
    }

    public void setPlcPort(Integer plcPort) {
        this.plcPort = plcPort;
    }

    public Integer getRefresh() {
        return refresh;
    }

    public void setRefresh(Integer refresh) {
        this.refresh = refresh;
    }

    public Integer getPlcType() {
        return plcType;
    }

    public void setPlcType(Integer plcType) {
        this.plcType = plcType;
    }
}