package com.entity;

import java.util.Date;

public class MoveStore {
    private String moveId;

    private String moveNo;

    private String orderId;

    private String orderDetailId;

    private String barCode;

    private String storeIdFrom;

    private String storeIdTo;

    private Date moveTime;

    private Integer status;

    public String getMoveId() {
        return moveId;
    }

    public void setMoveId(String moveId) {
        this.moveId = moveId == null ? null : moveId.trim();
    }

    public String getMoveNo() {
        return moveNo;
    }

    public void setMoveNo(String moveNo) {
        this.moveNo = moveNo == null ? null : moveNo.trim();
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
    }

    public String getStoreIdFrom() {
        return storeIdFrom;
    }

    public void setStoreIdFrom(String storeIdFrom) {
        this.storeIdFrom = storeIdFrom == null ? null : storeIdFrom.trim();
    }

    public String getStoreIdTo() {
        return storeIdTo;
    }

    public void setStoreIdTo(String storeIdTo) {
        this.storeIdTo = storeIdTo == null ? null : storeIdTo.trim();
    }

    public Date getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(Date moveTime) {
        this.moveTime = moveTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}