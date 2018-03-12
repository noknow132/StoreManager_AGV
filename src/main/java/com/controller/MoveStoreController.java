package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.IMoveStoreService;

@RestController
@RequestMapping("/MoveStoreController")
public class MoveStoreController {
	@Autowired
	private IMoveStoreService moveStoreService;

	//添加移库
	@RequestMapping("/insertMoveStore")
	public Map<String,Object> insertMoveStore(String storeFromId,String storeToId){
		return moveStoreService.insertMoveStore(storeFromId, storeToId);	
	}
	
	//删除移库
	@RequestMapping("/deleteMoveStore")
	public Map<String,Object> deleteMoveStore(String moveId){
		return moveStoreService.deleteMoveStore(moveId);	
	}
	
	//执行移库作业
	@RequestMapping("/startMoveStore")
	public Map<String, Object> startMoveStore(String moveIds[]) {
		return moveStoreService.startMoveStore(moveIds);
	}
	
	//查询预移库
	@RequestMapping("/searchtMoveStore")
	public List<Map<String, Object>> searchtMoveStore(String status) {
		return moveStoreService.searchtMoveStore(status);
	}
}
