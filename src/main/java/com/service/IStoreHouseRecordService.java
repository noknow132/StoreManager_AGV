package com.service;

import java.util.Map;

/**
 * 
 * @author DELL
 *
 */

public interface IStoreHouseRecordService {
	/**
	 * 查询根据仓位id查询该仓位的历史记录
	 * @param storeId 仓库id
	 * @param search 查询条件
	 * @param operateType 操作类型
	 * @return 执行结果
	 */
	Map<String, Object> searchStoreHouseRecord(String storeId,String search,String operateType);
	
	
	/**
	 * 批量删除历史记录
	 * @param ids 仓库id
	 * @return 执行结果
	 */
	Map<String, Object> delStoreHouseRecord(String ids);
	
}
