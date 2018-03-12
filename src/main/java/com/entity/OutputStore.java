package com.entity;

import java.util.Date;

public class OutputStore {
    private String outputStoreId;

    private String orderId;

    private String outputStoreNo;

    private String barCode;

    private String createstorehouseId;

    private String storeId;

    private Integer count;

    private String unit;

    private Date outputTime;

    private Integer statue;

    private String outletId;

    public String getOutputStoreId() {
        return outputStoreId;
    }

    public void setOutputStoreId(String outputStoreId) {
        this.outputStoreId = outputStoreId == null ? null : outputStoreId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOutputStoreNo() {
        return outputStoreNo;
    }

    public void setOutputStoreNo(String outputStoreNo) {
        this.outputStoreNo = outputStoreNo == null ? null : outputStoreNo.trim();
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
    }

    public String getCreatestorehouseId() {
        return createstorehouseId;
    }

    public void setCreatestorehouseId(String createstorehouseId) {
        this.createstorehouseId = createstorehouseId == null ? null : createstorehouseId.trim();
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Date getOutputTime() {
        return outputTime;
    }

    public void setOutputTime(Date outputTime) {
        this.outputTime = outputTime;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId == null ? null : outletId.trim();
    }
}