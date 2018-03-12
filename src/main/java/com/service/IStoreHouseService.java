package com.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.CreateStoreHouse;
import com.entity.StoreHouse;
import com.entity.Temp;
/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 建库对应的业务逻辑接口
 */
public interface IStoreHouseService {

	/**
	 * 查询仓库信息
	 * @param createstoreareaId 区位id
	 * @param storeName 仓库名字
	 * @param search 查询条件
	 * @return
	 */
	 Map<String, Object>  searchStoreHouse(String createstoreareaId,String search,String storeName);
	 
	 /**
	  * 查询空仓位
	  * @param storeNo 仓位号
	  * @return
	  */
	 List<Map<String,Object>>  searchEmptyStoreHouse(String storeNo);
	 
	 /**
	  * 查询指定建库下的空仓位信息
	  * @param storeId 仓库id
	  * @param storeNo 仓位编号
	  * @return
	  */
	 List<StoreHouse> searchEmptyStoreHouseByCreateStoreHouseId(String storeId,String storeNo);
	 
	/**
	 * 查询仓库信息
	 * @param storeStatus 仓位状态
	 * @param condition 筛选条件
	 * @return 仓库集
	 */
	 List<Map<String, Object>> searchStoreHouseByStoreStatus(String storeStatus,String condition);
	 
	 /**
	  * 查询不在预移库范围内的仓库(移库使用)
	  * @param condition 查询条件
	  * @return
	  */
	 List<Map<String, Object>> searchStoreHouseByNotInMoveStore(String condition);
	 
	 /**
	  * 查询仓位信息（纠正仓位信息功能查询）
	  * @param storeId 仓位id
	  * @return
	  */
	 Map<String, Object> searchStoreHouseByIdOnStorageMessage(String storeId);
	 
	 /**
	  * 修改仓位信息
	  * @param storeHouse 仓位实体
	  * @return
	  */
	 Map<String, Object> updateStoreHouse(StoreHouse storeHouse);
}