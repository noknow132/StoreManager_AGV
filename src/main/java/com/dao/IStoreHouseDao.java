package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.StoreHouse;
/**
 * @author 罗欢欢
 * @date 2018-1-4
 * @remark 仓库对应的数据逻辑接口
 */
public interface IStoreHouseDao {
	int deleteByPrimaryKey(String storeId);

	/**
	 * 新增仓库
	 * @param sHouse 仓库实体
	 * @return 新增数据条数
	 */
	int insertStoreHouse(StoreHouse sHouse);

	/**
	 * 检查有货仓库的数量
	 * @return
	 */
	int selectCountStoreHouseCountHasGoods();

	/**
	 * 批量新增仓库
	 * @param sHouses 仓库实体集合
	 * @return 新增数据条数
	 */
	int insertStoreHouses(@Param("sHouses")List<StoreHouse> sHouses);

	/**
	 * 根据订单号  ，仓位号或条形码 查找仓库中有货的仓库商品信息  
	 * @param search 查找条件
	 * @return 
	 */
	List<Map<String,Object>> selectHasGoodsBySearch(@Param("search")String search);


	/**
	 * 根据订单号  ，仓位号或条形码 查找仓库中有货的仓库商品信息  
	 * @param searchList 查找条件
	 * @return 
	 */
	List<Map<String,Object>> selectHasGoodsBySearchList(@Param("searchList")List<String> searchList);


	/**
	 * 根据建库id和订单号  ，仓位号或条形码 查找仓库中的信息  
	 * @param params 查找条件
	 * @return 
	 */
	List<Map<String,Object>> selectAllBySearch(Map<String,Object> params);


	/**
	 * 根据id查找库位
	 * @param storeId
	 * @return
	 */
	StoreHouse selectByStoreId(String storeId);

	/**
	 * 根据仓位编号和所属区位查找仓库
	 * @param storeNo  仓位编号
	 * @param createStoreAreaId  区位id
	 * @return
	 */
	StoreHouse selectByStoreNo(@Param("storeNo")String storeNo,@Param("createStoreAreaId")String createStoreAreaId);

	/**
	 * 根据区位号和建库id查找仓位
	 * @param storeNo
	 * @param createStoreHouseId
	 * @return
	 */
	StoreHouse selectByStoreNoAndCreateStoreHouseId(@Param("storeNo")String storeNo,@Param("createStoreHouseId")String createStoreHouseId);
	/**
	 * 查询空仓位
	 * @param createStoreHouseId 建库id
	 * @param storeNo 仓位号
	 * @return
	 */
	List<StoreHouse> searchEmptyStoreHouse(@Param("createStoreHouseId")String createStoreHouseId,@Param("storeNo")String storeNo);

	/**
	 * 查询空仓位郑州商院
	 * @param createStoreHouseId 建库id
	 * @return
	 */
	List<StoreHouse> searchEmptyStoreHouseZZ(@Param("createStoreHouseId")String createStoreHouseId);

	/**
	 * 修改仓库
	 * @param storeHouse 仓库实体
	 * @return 数字
	 */
	int updateStoreHouse(StoreHouse storeHouse);

	/**
	 * 查询仓库信息
	 * @param storeStatus 仓位状态
	 * @param condition 筛选条件
	 * @return
	 */
	List<Map<String, Object>> searchStoreHouseByStoreStatus(@Param("storeStatus")String storeStatus,@Param("condition")String condition);

	/**
	 * 查询不在预移库范围内的仓库(移库使用)
	 * @param condition 查询条件
	 * @return
	 */
	List<Map<String, Object>> searchStoreHouseByNotInMoveStore(@Param("condition")String condition);

	/**
	 * 根据建库id查找仓库
	 * @param createStoreId 建库id
	 * @return
	 */
	List<StoreHouse> searchStoreHouseByCreateStoreId(String createStoreId);

	/**
	 * 根据建库id查找空仓库
	 * @param createStoreId 建库id
	 * @return
	 */
	List<StoreHouse> searchEmptyStoreHouseByCreateStoreId(String createStoreId);

	/**
	 * 通过区位id 查找当前区位对应的所有仓位
	 * @param createStoreAreaId  区位id
	 * @return
	 */
	List<StoreHouse> searchStoreHouseByCreateStoreAreaId(String createStoreAreaId);

	/**
	 * 通过仓位号 删除仓位
	 * @param storeNo  仓位号
	 * @param createStoreAreaId  区位id
	 * @return
	 */
	int deleteStoreByStoreNoAndAreaId(String storeNo,String createStoreAreaId);

	/**
	 * 通过建库id删除所有对应的仓库
	 * @param createStoreHouseId 建库id
	 * @return
	 */
	int deleteStoreByCreateStoreHouseId(String createStoreHouseId);

	/**
	 * 通过区位id删除所有对应的仓库
	 * @param createStoreAreaId 区位id
	 * @return
	 */
	int deleteStoreByCreateStoreAreaId(String createStoreAreaId);

	/**
	 * 查询仓位信息（纠正仓位信息功能查询）
	 * @param storeId 仓位id
	 * @return
	 */
	Map<String, Object> searchStoreHouseByIdOnStorageMessage(String storeId);

	StoreHouse selectByPrimaryKey(String storeId);

	int updateByPrimaryKey(StoreHouse record);
}