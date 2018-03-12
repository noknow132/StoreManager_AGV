package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.service.IRunStepRecordService;

@RestController
@RequestMapping("/RunStepRecordController")
public class RunStepRecordController {
	@Autowired
	private IRunStepRecordService runStepRecordService;
	
	//查询运行步骤记录
	@RequestMapping(value="/searchRunStepRecord",method = RequestMethod.POST)
	public List<List<Map<String,Object>>> searchRunStepRecord(int type,String no){
		return runStepRecordService.searchRunStepRecord(type,no);
		
	}

}
