package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.StoreHouseRecord;

public interface IStoreHouseRecordDao {
	
	/**
	 * 根据仓位id，条件查找仓位记录表
	 * @param storeId 仓位id
	 * @param search  查找条件
	 * @param operateType  操作类型
	 * @return 查找到的记录
	 */
	List<Map<String,Object>> selectStoreHouseRecordByStoreId(@Param("storeId")String storeId,@Param("search")String search,@Param("operateType")String operateType);
	/**
	 * 根据仓位id，条件查找仓位记录表
	 * @param params 查找参数
	 * @return
	 */
	List<Map<String,Object>> selectStoreHouseRecordByStoreIdMap(Map<String,Object> params);

	/**
	 * 通过仓库id查找仓库存储记录
	 * @param storeId 仓库id
	 * @return
	 */
	List<StoreHouseRecord> searchStoreHouseRecordByStoreId(String storeId);
	
	/**
	   * 删除仓位的历史记录
	   * @param params
	   * @return 删除的条数
	   */
	int deleteStoreHouseRecordByIds(Map<String,Object> params);
	
	/**
	 * 添加仓库记录
	 * @param storeHouseRecord 仓库记录
	 * @return
	 */
	int insertStoreHouseRecord(StoreHouseRecord storeHouseRecord);
	
	/**
	 * 根据仓库id删除仓库存储记录
	 * @param storeId 仓库id
	 */
	void deleteStoreHouseRecordByStoreId(String storeId);
}