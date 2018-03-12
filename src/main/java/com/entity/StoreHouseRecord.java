package com.entity;

import java.util.Date;

public class StoreHouseRecord {
    private String recordId;

    private String storeId1;

    private String storeId2;

    private Integer operateType;

    private String barCode;

    private String orderNo;

    private Date operateTimeStart;

    private Date operateTimeEnd;

    private Integer count;

    private String orderId;

    private String orderDetailId;

    private String unit;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId == null ? null : recordId.trim();
    }

    public String getStoreId1() {
        return storeId1;
    }

    public void setStoreId1(String storeId1) {
        this.storeId1 = storeId1 == null ? null : storeId1.trim();
    }

    public String getStoreId2() {
        return storeId2;
    }

    public void setStoreId2(String storeId2) {
        this.storeId2 = storeId2 == null ? null : storeId2.trim();
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Date getOperateTimeStart() {
        return operateTimeStart;
    }

    public void setOperateTimeStart(Date operateTimeStart) {
        this.operateTimeStart = operateTimeStart;
    }

    public Date getOperateTimeEnd() {
        return operateTimeEnd;
    }

    public void setOperateTimeEnd(Date operateTimeEnd) {
        this.operateTimeEnd = operateTimeEnd;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }
}