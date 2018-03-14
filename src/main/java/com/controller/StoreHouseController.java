package com.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.entity.StoreHouse;
import com.service.IStoreHouseService;
/**
 * @author 罗欢欢
 * @date 2018-1-8
 * @remark 仓库业务
 */

@RestController
@RequestMapping("/storeHouse")
public class StoreHouseController {
	@Autowired
	private IStoreHouseService storeHouseService;
	

	/**
	 * 查询仓库信息
	 * @param createstorehouseId 建库id
	 * @param search 查询条件
	 * @return
	 */
	@RequestMapping(value="/searchStoreHouse",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  searchCreateStoreHouse(String createstoreareaId,String search,String storeName){
		    return storeHouseService.searchStoreHouse(createstoreareaId,search,storeName);
	}
	

	
	//查询空仓位信息
	@RequestMapping(value = "/searchEmptyStoreHouse")
	public List<Map<String,Object>> searchEmptyStoreHouse(String storeNo) {
		return storeHouseService.searchEmptyStoreHouse(storeNo);
	}
	
	//查询指定建库下的空仓位信息
	@RequestMapping(value = "/searchEmptyStoreHouseByCreateStoreHouseId")
	public List<StoreHouse> searchEmptyStoreHouseByCreateStoreHouseId(String storeId,String storeNo) {
		return storeHouseService.searchEmptyStoreHouseByCreateStoreHouseId(storeId,storeNo);
	}
	
	//查询仓库信息
	@RequestMapping(value = "/searchStoreHouseByStoreStatus")
	public List<Map<String, Object>> searchStoreHouseByStoreStatus(String storeStatus,String condition) {
		return storeHouseService.searchStoreHouseByStoreStatus(storeStatus,condition);
	}
	
	//查询不在预移库范围内的仓库(移库使用)
	@RequestMapping(value = "/searchStoreHouseByNotInMoveStore")
	public List<Map<String, Object>> searchStoreHouseByNotInMoveStore(String condition) {
		return storeHouseService.searchStoreHouseByNotInMoveStore(condition);
	}
	
	//查询仓位信息（纠正仓位信息功能查询）
	@RequestMapping(value = "/searchStoreHouseByIdOnStorageMessage")
	public Map<String, Object> searchStoreHouseByIdOnStorageMessage(String storeId) {
		return storeHouseService.searchStoreHouseByIdOnStorageMessage(storeId);
	}
	
	//纠正仓位信息
	@RequestMapping(value = "/updateStoreHouse")
	public Map<String, Object> updateStoreHouse(StoreHouse storeHouse) {
		return storeHouseService.updateStoreHouse(storeHouse);
	}
	
}
