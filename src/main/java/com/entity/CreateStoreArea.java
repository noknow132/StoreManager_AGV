package com.entity;

import java.util.Date;

public class CreateStoreArea {
    private String createstoreareaId;

    private String createstorehouseId;

    private String areaName;

    private Integer rowsCount;

    private Integer rowsStart;

    private Integer columnsCount;

    private Integer columnsStart;

    private Integer sequence;

    private String creator;

    private Date createTime;

    private String robotNo;

    public String getCreatestoreareaId() {
        return createstoreareaId;
    }

    public void setCreatestoreareaId(String createstoreareaId) {
        this.createstoreareaId = createstoreareaId == null ? null : createstoreareaId.trim();
    }

    public String getCreatestorehouseId() {
        return createstorehouseId;
    }

    public void setCreatestorehouseId(String createstorehouseId) {
        this.createstorehouseId = createstorehouseId == null ? null : createstorehouseId.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public Integer getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(Integer rowsCount) {
        this.rowsCount = rowsCount;
    }

    public Integer getRowsStart() {
        return rowsStart;
    }

    public void setRowsStart(Integer rowsStart) {
        this.rowsStart = rowsStart;
    }

    public Integer getColumnsCount() {
        return columnsCount;
    }

    public void setColumnsCount(Integer columnsCount) {
        this.columnsCount = columnsCount;
    }

    public Integer getColumnsStart() {
        return columnsStart;
    }

    public void setColumnsStart(Integer columnsStart) {
        this.columnsStart = columnsStart;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRobotNo() {
        return robotNo;
    }

    public void setRobotNo(String robotNo) {
        this.robotNo = robotNo == null ? null : robotNo.trim();
    }
}