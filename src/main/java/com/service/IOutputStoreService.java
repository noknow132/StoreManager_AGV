package com.service;

import java.util.List;
import java.util.Map;

import com.entity.CreateStoreHouse;
import com.entity.OutputStoreDto;
import com.entity.Temp;
/**
 * @author 罗欢欢
 * @date 2018-1-5
 * @remark 出库的业务逻辑接口
 */
public interface IOutputStoreService {
	/**
	 * 查找要出库的列表
	 * @param search 查找条件
	 * @return 返回执行结果和数据
	 */
	Map<String, Object> searchOutputStoreHouseList(String  search);
	
	/**
	 * 根据id进行出库操作
	 * @param opds 仓库业务实体数组
	 * @return 执行结果
	 */
	Map<String, Object> addOutputStores(OutputStoreDto[] opds);
	  
}