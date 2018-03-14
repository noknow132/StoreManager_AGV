package com.entity;

import java.util.Date;

public class InputStore {
    private String inputStoreId;

    private String orderId;

    private String orderDetailId;

    private String inputStoreNo;

    private String createstorehouseId;

    private String storeId;

    private String barCode;

    private Integer count;

    private String unit;

    private Date inputTime;

    private Integer statue;
    
    private String placeId;

    public String getInputStoreId() {
        return inputStoreId;
    }

    public void setInputStoreId(String inputStoreId) {
        this.inputStoreId = inputStoreId == null ? null : inputStoreId.trim();
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

    public String getInputStoreNo() {
        return inputStoreNo;
    }

    public void setInputStoreNo(String inputStoreNo) {
        this.inputStoreNo = inputStoreNo == null ? null : inputStoreNo.trim();
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
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

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

	public String getPlaceId() {
		return this.placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
}