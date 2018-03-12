package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.CreateStoreArea;
import com.entity.CreateStoreHouse;
import com.entity.StoreHouse;

public interface ICreateStoreAreaDao {
	
	/**
	 * 根据仓位id查找库区实体
	 * @param storeId 仓位编号
	 * @return 库区实体
	 */
	CreateStoreArea selectCreateStoreAreaByStoreId(@Param("storeId")String  storeId);
	
	/**
	 * 新增区位
	 * @param createStoreArea 区位实体
	 * @return
	 */
	int insertCreateStoreArea(CreateStoreArea createStoreArea); 
	
	/**
	 * 根据建库 查找区位 表格使用
	 * @param CreateStoreHouseId 建库id
	 * @return
	 */
	List<Map<String,Object>> searchCreateStoreAreaByCreateStoreHouseId(String CreateStoreHouseId);
	
	/**
	 * 根据建库 查找区位
	 * @param CreateStoreHouseId 建库id
	 * @return
	 */
	List<CreateStoreArea> selectCreateStoreAreaByCreateStoreHouseId(String CreateStoreHouseId);
	
	/**
	 * 检查区位是否存在
	 * @param createstorehouseId 建库id
	 * @param areaName 区位名
	 * @return
	 */
	CreateStoreArea checkAreaNameExist(@Param("createstorehouseId")String createstorehouseId,@Param("areaName")String areaName);
	
	/**
	 * 根据区位id 查询区位信息
	 * @param createStoreAreaId 区位id
	 * @return
	 */
	CreateStoreArea selectCreateStoreAreaById(String createStoreAreaId);
	
	/**
	 * 修改区位
	 * @param createStoreArea 区位实体
	 * @return
	 */
	int updateCreateStoreArea(CreateStoreArea createStoreArea);
	
	/**
	 * 根据id删除区位
	 * @param createStoreAreaId 区位id
	 * @return
	 */
	int deleteCreateStoreAreaById(String createStoreAreaId);
	
	

}