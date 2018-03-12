package com.dao;

import java.util.List;
import java.util.Map;

import com.entity.MoveStore;

public interface IMoveStoreDao {
	/**
	 * 添加移库
	 * @param moveStore 移库实体
	 * @return
	 */
	int insertMoveStore(MoveStore moveStore);
	
	/**
	 * 根据移库id查找移库
	 * @param moveId 移库id
	 * @return 移库实体
	 */
	MoveStore searchMoveStoreById(String moveId);
	
	/**
	 * 根据移库单号查找移库
	 * @param moveNo 移库单号
	 * @return 移库实体
	 */
	MoveStore selectMoveStoreByNo(String moveNo);
	
	/**
	 * 修改移库
	 * @param moveStore 移库实体
	 * @return
	 */
	int updateMoveStore(MoveStore moveStore);
	
	/**
	 * 查询预移库
	 * @param status 单据状态
	 * @return
	 */
	List<Map<String,Object>> searchMoveStore(String status);
	
	/**
	 * 根据起始仓库id删除移库单
	 * @param storeIdFrom
	 */
	void deleteMoveStoreByStoreIdFrom(String storeIdFrom);
	
	/**
	 * 通过仓库id查找移库单记录的单号
	 * @param storeId 仓库id
	 * @return
	 */
	List<String> searchMoveStoreNoByStoreId(String storeId);
}