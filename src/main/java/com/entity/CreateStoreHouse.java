package com.entity;

import java.util.Date;
/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 建库实体
 */
public class CreateStoreHouse {
	private String createstorehouseId;

    private String storeName;

    private String storeType;

    private String storeAddress;

    private String storeMaster;

    private String masterTel;

    private String areaName;

    private Integer rowsCount;

    private Integer rowsStart;

    private Integer columnsCount;

    private Integer columnsStart;

    private Integer sequence;

    private String creator;

    private Date createTime;

    private String robotNo;

    public String getCreatestorehouseId() {
        return createstorehouseId;
    }

    public void setCreatestorehouseId(String createstorehouseId) {
        this.createstorehouseId = createstorehouseId == null ? null : createstorehouseId.trim();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType == null ? null : storeType.trim();
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress == null ? null : storeAddress.trim();
    }

    public String getStoreMaster() {
        return storeMaster;
    }

    public void setStoreMaster(String storeMaster) {
        this.storeMaster = storeMaster == null ? null : storeMaster.trim();
    }

    public String getMasterTel() {
        return masterTel;
    }

    public void setMasterTel(String masterTel) {
        this.masterTel = masterTel == null ? null : masterTel.trim();
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