package com.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.service.IStoreHouseRecordService;
/**
 * @author 罗欢欢
 * @date 2018-1-8
 * @remark 仓库业务
 */

@RestController
@RequestMapping("/storeHouseRecord")
public class StoreHouseRecordController {
	@Autowired
	private IStoreHouseRecordService storeHouseRecordService;
	

	/**
	 * 根据区位id查找历史记录
	 * @param storeId  区位id
	 * @param search   查找条件
	 * @param operateType  操作类型
	 * @return 执行结果
	 */
	@RequestMapping(value="/searchStoreHouseRecord",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  searchStoreHouse(String storeId,String search,String operateType){
		    return storeHouseRecordService.searchStoreHouseRecord(storeId,search,operateType);
	}
	/**
	 * 删除区位信息
	 * @param ids 建库id
	 * @return
	 */
	@RequestMapping(value="/delStoreHouseRecord",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  delStoreHouseRecord(String ids){
		    return storeHouseRecordService.delStoreHouseRecord(ids);
	}
	
}
