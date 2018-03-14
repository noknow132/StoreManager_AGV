package com.entity;

public class OutinPlace {
    private String id;

    private Integer placeNo;

    private String createstorehouseId;

    private String createstoreareaId;

    private Integer type;

    private Integer placeHeight;

    private Integer column;

    private String outNo;

    private Integer isUsed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getPlaceNo() {
        return placeNo;
    }

    public void setPlaceNo(Integer placeNo) {
        this.placeNo = placeNo;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPlaceHeight() {
        return placeHeight;
    }

    public void setPlaceHeight(Integer placeHeight) {
        this.placeHeight = placeHeight;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo == null ? null : outNo.trim();
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }
}