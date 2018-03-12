package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.CreateStoreHouse;

/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 建库对应的数据逻辑接口
 */
public interface ICreateStoreHouseDao {
	int deleteByPrimaryKey(String createstorehouseId);
	
	/**
	 * 新增建库
	 * @param csh 建库实体
	 * @return
	 */
	int insertCreateStoreHouse(CreateStoreHouse csh);
	
	/**
	 * 查询仓库 一个仓库
	 * @return 建库实体
	 */
	CreateStoreHouse selectCreateStoreHouseOne();
	
	/**
	 * 查询所有建库
	 * @return
	 */
	List<CreateStoreHouse> searchCreateStoreHouseAll();
	
	/**
	 * 修改仓库
	 * @param params
	 * @return 修改的记录条数
	 */
	int updateCreateStoreHouseBySelective(Map<String, Object> params);
	/**
	 * 根据主键查找建库实体
	 * @param createstorehouseId 建库id
	 * @return 建库实体
	 */
	CreateStoreHouse selectCreateStoreHouseById(String createstorehouseId);
	
	/**
	 * 根据根据区位名称查找建库实体
	 * @param areaName 建库id
	 * @return 建库实体
	 */
	CreateStoreHouse selectCreateStoreHouseByAreaName(@Param("areaName")String  areaName);
	
	/**
	 * 根据仓位编号查找建库实体
	 * @param storeNo 仓位编号
	 * @return 建库实体
	 */
	CreateStoreHouse selectCreateStoreHouseByStoreNo(@Param("storeNo")String  storeNo);
	

	
	/**
	 * 查找所有区位的信息
	 * @return 区位信息列表
	 */
	List<Map<String,Object>> selectAreaNames();
	
	/**
	 * 根据名称分组查询仓库
	 * @return
	 */
	List<Map<String,Object>> searchCreateStoreHouseGroupByStoreName();
	
	/**
	 * 根据建库名称查找建库
	 * @return
	 */
	List<CreateStoreHouse> searchCreateStoreHouseByStoreName(String storeName);
	
	/**
	 * 仓库基本信息
	 * @param createStoreHouseId 建库id
	 * @return
	 */
	Map<String, Object> searchStoreBaseInfo(String createStoreHouseId);
	
	/**
	 * 删除区位
	 * @param createStoreId 建库id
	 */
	int deleteCreateStoreHouseById(String createStoreId);
	

	int insertSelective(CreateStoreHouse record);
	
	CreateStoreHouse selectByPrimaryKey(String createstorehouseId);

	int updateByPrimaryKeySelective(CreateStoreHouse record);

	/**
	 * 修改建库
	 * @param createStoreHouse 建库实体
	 * @return
	 */
	int updateCreateStoreHouse(CreateStoreHouse createStoreHouse);
}