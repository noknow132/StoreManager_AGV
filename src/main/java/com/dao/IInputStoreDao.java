package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.entity.InputStore;

public interface IInputStoreDao {
	/**
	 * 入库添加
	 * @param inputStore 入库对对象
	 * @return 数字
	 */
	int insertGoods(InputStore inputStore);
	
	/**
	 * 批量删除入库作业
	 * @param ids 入库作业id集合
	 * @return 数字
	 */
	int deleteManyInputStoreById(@Param("ids")String[] ids);
	
	/**
	 * 查询入库作业
	 * @param status 单据状态
	 * @return
	 */
	List<Map<String, Object>> searchInputStore(String status);
	
	/**
	 * 通过id查找入库作业
	 * @param id 入库id
	 * @return
	 */
	InputStore searchInpuStoreById(String id);
	
	/**
	 * 通过inputStoreNo查找入库作业
	 * @param inputStoreNo 入库单号
	 * @return 入库单实体
	 */
	InputStore selectInpuStoreByNo(String inputStoreNo);
	
	/**
	 * 修改入库作业
	 * @param inputStore 入库对象
	 * @return
	 */
	int updateInputStore(InputStore inputStore);
	
	/**
	 * 检查条形码是否存在
	 * @param barCode 条码
	 * @return
	 */
	int checkBarCode(String barCode);
	
	/**
	 * 通过仓库id查找最新的入库单记录
	 * @param storeId 仓库id
	 * @return
	 */
	InputStore searchInputStoreByStoreId(String storeId);
	
	/**
	 * 根据仓库id删除入库订单
	 * @param storeId 仓库id
	 */
	void deleteInputStoreByStoreId(String storeId);
	
	/**
	 * 通过仓库id查找入库单记录的单号
	 * @param storeId 仓库id
	 * @return
	 */
	List<String> searchInputStoreNoByStoreId(String storeId);
}