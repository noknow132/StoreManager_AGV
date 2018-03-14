package com.service;

import java.util.List;
import java.util.Map;

import com.entity.CreateStoreArea;

public interface ICreateStoreAreaService {
	/**
	 * 新增区位
	 * @param createStoreArea 区位实体
	 * @param userId 登录人id
	 * @param createStoreHouseId 建库id
	 * @return
	 */
	Map<String,Object> addCreateStoreArea(CreateStoreArea createStoreArea,String userId);
	
	/**
	 * 检查区位是否存在
	 * @param areaName 区位名称
	 * @param createstorehouseId 建库id
	 */
	Map<String,Boolean> checkAreaNameExist(String areaName,String createstorehouseId);
	
	/**
	 * 根据建库 查找区位
	 * @param CreateStoreHouseId 建库id
	 * @return
	 */
	List<Map<String,Object>> searchCreateStoreAreaByCreateStoreHouseId(String CreateStoreHouseId);
	
	/**
	 * 根据区位id 查询区位信息
	 * @param createStoreAreaId 区位id
	 * @return
	 */
	CreateStoreArea selectCreateStoreAreaById(String createStoreAreaId);

}
