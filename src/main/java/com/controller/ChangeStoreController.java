package com.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.service.IChangeStoreService;
/**
 * @author 罗欢欢
 * @date 2018-1-15
 * @remark 调库业务
 */

@RestController
@RequestMapping("/changeStore")
public class ChangeStoreController {
	@Autowired
	private IChangeStoreService changeStoreService;
	//查找要调库的列表
	@RequestMapping(value="/searchChangeStoreBySearch",method = RequestMethod.POST)
	public Map<String, Object> searchChangeStoreBySearch(String search){
		return  changeStoreService.searchChangeStoreList(search);
	}
	//查找调库的列表
	@RequestMapping(value="/searchChangeStoreOrderList",method = RequestMethod.POST)
	public Map<String, Object> searchChangeStoreOrderList(Integer status){
		return  changeStoreService.searchChangeStoreOrderList(status);
	}
	//调库
	@RequestMapping(value="/addChangeStores",method = RequestMethod.POST)
	public Map<String, Object> addChangeStores(@RequestBody String[] changeIds ){
		return changeStoreService.addChangeStores(changeIds);
	}

	//增加调库单
	@RequestMapping(value="/insertChangeStore",method = RequestMethod.POST)
	public Map<String, Object> insertChangeStore(String storeIdFrom,String storeIdTo ){
		return changeStoreService.insertChangeStore(storeIdFrom,storeIdTo);
	}

	//删除调库单
	@RequestMapping(value="/delChangeStore",method = RequestMethod.POST)
	public Map<String, Object> delChangeStore(String changeId ){
		return changeStoreService.delChangeStore(changeId);
	}
}
