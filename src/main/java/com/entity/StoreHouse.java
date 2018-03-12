package com.entity;

public class StoreHouse {
    private String storeId;

    private String createstorehouseId;

    private String createstoreareaId;

    private String storeNo;

    private Integer storeStatue;

    private String goodNo;

    private String orderId;

    private String orderDetailId;

    private Integer count;

    private String unit;

    private String remark;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    public String getCreatestorehouseId() {
        return createstorehouseId;
    }

    public void setCreatestorehouseId(String createstorehouseId) {
        this.createstorehouseId = createstorehouseId == null ? null : createstorehouseId.trim();
    }

    public String getCreatestoreareaId() {
        return createstoreareaId;
    }

    public void setCreatestoreareaId(String createstoreareaId) {
        this.createstoreareaId = createstoreareaId == null ? null : createstoreareaId.trim();
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo == null ? null : storeNo.trim();
    }

    public Integer getStoreStatue() {
        return storeStatue;
    }

    public void setStoreStatue(Integer storeStatue) {
        this.storeStatue = storeStatue;
    }

    public String getGoodNo() {
        return goodNo;
    }

    public void setGoodNo(String goodNo) {
        this.goodNo = goodNo == null ? null : goodNo.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId == null ? null : orderDetailId.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}