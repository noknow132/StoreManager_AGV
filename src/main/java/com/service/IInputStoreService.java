package com.service;

import java.util.Map;

import com.entity.InputStore;

public interface IInputStoreService {
	/**
	 * 入库添加
	 * @param inputStore 入库对象
	 * @return 结果集
	 */
	Map<String, Object> insertGoods(InputStore inputStore);
	
	/**
	 * 批量删除入库作业
	 * @param ids 入库作业id
	 * @return 结果集
	 */
	Map<String, Object> deleteManyInputStoreById(String[] ids);
	
	/**
	 * 查询入库作业
	 * @param status 单据状态
	 * @return 入库集合
	 */
	Map<String, Object> searchInputStore(String status);
	
	/**
	 * 执行入库作业
	 * @param ids 入库id集合
	 * @return 结果集
	 */
	Map<String, Object> startInputStore(String ids[]);
	
	/**
	 * 检查条形码是否存在
	 * @param barCode
	 * @return
	 */
	int checkBarCode(String barCode);
}
