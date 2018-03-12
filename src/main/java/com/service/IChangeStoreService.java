package com.service;

import java.util.List;
import java.util.Map;

import com.entity.CreateStoreHouse;
import com.entity.OutputStoreDto;
import com.entity.Temp;
/**
 * @author 罗欢欢
 * @date 2018-1-5
 * @remark 调库的业务逻辑接口
 */
public interface IChangeStoreService {
	/**
	 * 查找要调库的列表
	 * @param search 查找条件
	 * @return 返回执行结果和数据
	 */
	Map<String, Object> searchChangeStoreList(String  search);
	/**
	 * 查找调库的列表
	 * @param status 状态
	 * @return 返回执行结果和数据
	 */
	Map<String, Object> searchChangeStoreOrderList(Integer status);
	/**
	 * 根据id进行调库操作
	 * @param changeIds 调库单id数组
	 * @return 执行结果
	 */
	Map<String, Object> addChangeStores(String[] changeIds );
	
	/**
	 * 增加调库单
	 * @param storeIdFrom 起始仓库id
	 * @param storeIdTo 目标仓库id
	 * @return 执行结果
	 */
	Map<String, Object> insertChangeStore(String  storeIdFrom,String  storeIdTo);
	  
	/**
	 * 删除调库单
	 * @param changeId 调库单id
	 * @return 执行结果
	 */
	Map<String, Object> delChangeStore(String changeId );
}