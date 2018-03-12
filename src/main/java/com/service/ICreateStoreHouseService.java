package com.service;

import java.util.List;
import java.util.Map;

import com.entity.CreateStoreArea;
import com.entity.CreateStoreHouse;
import com.entity.Temp;
/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 建库对应的业务逻辑接口
 */
public interface ICreateStoreHouseService {
	
	/**
	 * 新增仓库
	 * @param cStoreHouse  建库表实体
	 * @param userId  登录id
	 * @return 执行结果
	 */
	Map<String, Object> addCreateStoreHouse(CreateStoreHouse cStoreHouse,String userId);
	
	/**
	 * 新增仓库
	 * @param cStoreHouse 建库表实体
	 * @param userId 登录id
	 * @return
	 */
	Map<String, Object> insertCreateStoreHouse(CreateStoreHouse cStoreHouse,String userId);
	
	/**
	 * 编辑区位
	 * @param cStoreHouse 建库表实体
	 */
	Map<String, Object> editCreateStoreHouse2(CreateStoreArea createStoreArea);
	
	/**
	 * 编辑仓库
	 * @param cStoreHouse  建库表实体
	 * @return 执行结果
	 */
	Map<String, Object> editCreateStoreHouse(CreateStoreHouse cStoreHouse);
	
	/**
	 * 查询建库信息
	 * @return 执行结果
	 */
	Map<String, Object>   searchCreateStoreHouse();
	
	/**
	 * 查找所有的区位名字
	 * @return
	 */
	Map<String, Object>  searchAreaNames();
	
	/**
	 * 根据区位名称查找建库信息
	 * @param areaName 区位名称
	 * @return
	 */
	Map<String, Object>  searchCreateStoreHouseByAreaName(String areaName);	
	
	/**
	 * 根据主键查找建库信息
	 * @param createstorehouseId 建库id
	 * @return
	 */
	Map<String, Object>   searchCreateStoreHouseByCshId(String createstorehouseId);
	
	/**
	 * 检查区位名是否存在
	 * @param areaName 区位名称
	 * @param createstorehouseId 建库id
	 * @return
	 */
	Map<String,Boolean> checkAreaNameExist(String areaName,String createstorehouseId);
	
	/**
	 * 根据名称分组查询仓库
	 * @return
	 */
	List<Map<String,Object>> searchCreateStoreHouseGroupByStoreName();
	
	/**
	 * 根据建库名称查找建库
	 * @param storeName 建库名称
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
	 * 根据建库id查找建库
	 * @param createStoreId
	 * @return
	 */
	CreateStoreHouse selectCreateStoreHouseById(String createStoreId);
	
	/**
	 * 删除区位
	 * @param createstoreareaId 区位id
	 * @return
	 */
	Map<String,Object> deleteCreateStoreAreaById(String createstoreareaId);
	
	/**
	 * 删除仓库
	 * @param createstorehouseId 建库id
	 * @return
	 */
	Map<String,Object> deleteCreateStoreHouseById(String createstorehouseId);
	
	/**
	 * 查询已建仓库数目
	 * @return
	 */
	int searchCreateStoreHouseAllCount();
	
	
}