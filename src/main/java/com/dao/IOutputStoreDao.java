package com.dao;

import java.util.List;

import com.entity.InputStore;
import com.entity.OutputStore;

public interface IOutputStoreDao {
	int deleteByPrimaryKey(String outputStoreId);
	/**
	 * 插入出库单
	 * @param os 出库单
	 * @return 插入的条数
	 */
	int insertOutputStore(OutputStore os);

	/**
	 * 批量插入出库单
	 * @param oss 出库单列表
	 * @return 插入的条数
	 */
	int insertOutputStores(List<OutputStore> oss);

	/**
	 * 	根据出库单号查找出库单
	 * @param outputStoreNo 出库单号
	 * @return 出库单实体
	 */
	OutputStore selectOutputStoreByNo(String outputStoreNo);
	
	/**
	 * 更新操作
	 * @param outputStore 出库单实体
	 * @return 更新的条数
	 */
	int updateOutputStoreByIdSelective(OutputStore outputStore);
	
	/**
	 * 通过仓库id查找最新的入库单记录
	 * @param storeId 仓库id
	 * @return
	 */
	InputStore searchInputStoreByStoreId(String storeId);
	
	OutputStore searchOutputStoreByStoreId(String storeId);
	
	/**
	 * 根据仓库id删除出库订单
	 * @param storeId 仓库id
	 */
	void deleteOutPutStoreByStoreId(String storeId);
	
	/**
	 * 通过仓库id查找出库单记录的单号
	 * @param storeId 仓库id
	 * @return
	 */
	List<String> searchOutPutStoreNoByStoreId(String storeId);


	int insertSelective(OutputStore record);


	OutputStore selectByPrimaryKey(String outputStoreId);

	int updateByPrimaryKeySelective(OutputStore record);

	int updateByPrimaryKey(OutputStore record);
}