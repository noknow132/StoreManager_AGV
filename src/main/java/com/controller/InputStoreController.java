package com.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.InputStore;
import com.service.IInputStoreService;

@RestController
@RequestMapping("/InputStoreController")
public class InputStoreController {
	@Autowired
	private IInputStoreService inputStoreService;
	
	//入库添加
	@RequestMapping("/insertGoods")
	public Map<String, Object> insertGoods(InputStore inputStore) {
		return inputStoreService.insertGoods(inputStore);
	}
	
	//批量删除入库作业
	@RequestMapping("/deleteManyInputStoreById")
	public Map<String, Object> deleteManyInputStoreById(String[] ids) {
		return inputStoreService.deleteManyInputStoreById(ids);
	}
	
	//查询入库作业
	@RequestMapping("/searchInputStore")
	public Map<String, Object> searchInputStore(String status) {
		return inputStoreService.searchInputStore(status);
	}
	
	//执行入库作业
	@RequestMapping("/startInputStore")
	public Map<String, Object> startInputStore(String ids[]) {
		return inputStoreService.startInputStore(ids);
	}
	
	//检查条形码是否存在
	@RequestMapping("/checkBarCode")
	public int checkBarCode(String barCode) {
		return inputStoreService.checkBarCode(barCode);
	}
	
}
