package com.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entity.OutputStoreDto;
import com.service.IOutputStoreService;
/**
 * @author 罗欢欢
 * @date 2018-1-5
 * @remark 出库的功能
 */

@RestController
@RequestMapping("/outputStore")
public class OutputStoreController {
	@Autowired
	private	IOutputStoreService outputStoreService ;

	//查找要出库的列表
	@RequestMapping(value="/searchOutputStoreBySearch",method = RequestMethod.POST)
	public Map<String, Object> searchOutputStoreBySearch(String search){
		return  outputStoreService.searchOutputStoreHouseList(search);
	}

	/*//出库
	@RequestMapping(value="/addOutputStore",method = RequestMethod.POST)
	public Map<String, Object> addOutputStores(String ids){		
		return  outputStoreService.addOutputStores(ids);
	}*/

	
	//出库
	@RequestMapping(value="/addOutputStore",method = RequestMethod.POST)
	public Map<String, Object> addOutputStores(@RequestBody OutputStoreDto[] opds ){
		
		return outputStoreService.addOutputStores(opds);
		//return  outputStoreService.addOutputStores(ids);
	}
}
