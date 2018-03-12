package com.entity;
/**
 * @author 罗欢欢
 * @date 2018-1-12
 * @remark 出库业务实体
 */

public class OutputStoreDto {
	private String storeId;
	private String storeNo;
	private String outletId;
	
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public OutputStoreDto(String storeId, String storeNo, String outletId) {
		this.storeId = storeId;
		this.storeNo = storeNo;
		this.outletId = outletId;
	}

	public OutputStoreDto() {
	}
	
	
}