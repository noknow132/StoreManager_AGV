package com.entity;

public class ConfigParam {
	  private Integer id;

	    private Integer reset;

	    private Integer warn;

	    private Integer warnTag;

	    private Integer runAgv;

	    private String rebotCode;

	    private String programName;

	    private String localIp;

	    private String plcIp;

	    private Integer plcPort;

	    private String mac;

	    private String regEdit;

	    private String regEditCopy;

	    private Integer isRun;

	    private Integer isScan;

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public Integer getReset() {
	        return reset;
	    }

	    public void setReset(Integer reset) {
	        this.reset = reset;
	    }

	    public Integer getWarn() {
	        return warn;
	    }

	    public void setWarn(Integer warn) {
	        this.warn = warn;
	    }

	    public Integer getWarnTag() {
	        return warnTag;
	    }

	    public void setWarnTag(Integer warnTag) {
	        this.warnTag = warnTag;
	    }

	    public Integer getRunAgv() {
	        return runAgv;
	    }

	    public void setRunAgv(Integer runAgv) {
	        this.runAgv = runAgv;
	    }

	    public String getRebotCode() {
	        return rebotCode;
	    }

	    public void setRebotCode(String rebotCode) {
	        this.rebotCode = rebotCode == null ? null : rebotCode.trim();
	    }

	    public String getProgramName() {
	        return programName;
	    }

	    public void setProgramName(String programName) {
	        this.programName = programName == null ? null : programName.trim();
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

	    public String getMac() {
	        return mac;
	    }

	    public void setMac(String mac) {
	        this.mac = mac == null ? null : mac.trim();
	    }

	    public String getRegEdit() {
	        return regEdit;
	    }

	    public void setRegEdit(String regEdit) {
	        this.regEdit = regEdit == null ? null : regEdit.trim();
	    }

	    public String getRegEditCopy() {
	        return regEditCopy;
	    }

	    public void setRegEditCopy(String regEditCopy) {
	        this.regEditCopy = regEditCopy == null ? null : regEditCopy.trim();
	    }

	    public Integer getIsRun() {
	        return isRun;
	    }

	    public void setIsRun(Integer isRun) {
	        this.isRun = isRun;
	    }

	    public Integer getIsScan() {
	        return isScan;
	    }

	    public void setIsScan(Integer isScan) {
	        this.isScan = isScan;
	    }
	}