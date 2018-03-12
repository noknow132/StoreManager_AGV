package com.entity;

import java.util.Date;

public class WorkType {
    private String typeId;

    private String typeName;

    private Integer typeStatue;

    private Date createTime;
    
    private Date updateTime;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId == null ? null : typeId.trim();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Integer getTypeStatue() {
        return typeStatue;
    }

    public void setTypeStatue(Integer typeStatue) {
        this.typeStatue = typeStatue;
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