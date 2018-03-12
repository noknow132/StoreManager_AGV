package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.WorkType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.service.IWorkTypeService;

@RestController
@RequestMapping("/WorkTypeController")
public class WorkTypeController {
	@Autowired
	private IWorkTypeService workTypeService;
	
	//查询所有作业类型
	@SuppressWarnings("unchecked")
	@RequestMapping("/searchWorkTypeBootstrap")
	private Map <String ,Object> searchWorkTypeBootstrap(Integer limit,Integer offerset ){
		//JsonObject json=new JsonObject();
		Map <String ,Object> map=new HashMap();
		List<WorkType> list = workTypeService.searchWorkType();
		map.put("rows", list);
		map.put("total", 5);
		
		return map;
				
	}
	
	
	
	//查询所有作业类型
	@RequestMapping("/searchWorkType")
	private List<WorkType> searchWorkType(){
		return workTypeService.searchWorkType();
	}
	
	//新增或修改作业类型
	@RequestMapping("/saveWorkType")
	private Map<String,Object> saveWorkType(WorkType workType){
		return workTypeService.saveWorkType(workType);
	}
	
	//启用或禁用作业类型
	@RequestMapping("/updateWorkTypeById")
	private Map<String,Object> updateWorkTypeById(String workTypeId,String isUsed){
		return workTypeService.updateWorkTypeById(workTypeId,isUsed);
	}
	
	//根据id查找作业类型
	@RequestMapping("/searchWorkTypeById")
	private WorkType searchWorkTypeById(String workTypeId){
		return workTypeService.searchWorkTypeById(workTypeId);
	}
}
	