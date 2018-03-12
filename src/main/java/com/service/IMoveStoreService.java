package com.service;

import java.util.List;
import java.util.Map;

public interface IMoveStoreService {
	/**
	 * 添加移库
	 * @param storeFromId 起始仓库id
	 * @param storeToId 目标仓库id
	 * @return 操作结果
	 */
	Map<String,Object> insertMoveStore(String storeFromId,String storeToId);
	
	/**
	 * 执行移库作业
	 * @param moveIds 移库id集合
	 * @return
	 */
	Map<String, Object> startMoveStore(String moveIds[]);
	
	/**
	 * 删除移库
	 * @param moveId 移库id
	 * @return 结果集
	 */
	Map<String,Object> deleteMoveStore(String moveId);
	
	/**
	 * 查询预移库
	 * @param status 单据状态
	 * @return List
	 */
	List<Map<String, Object>> searchtMoveStore(String status);
}
