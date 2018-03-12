package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.service.IWarnRecordService;

@RestController
@RequestMapping("/WarnRecordController")
public class WarnRecordController {
	@Autowired
	private IWarnRecordService warnRecordService;
	
	//查询报警记录
	@RequestMapping(value="/searchWarnRecord",method = RequestMethod.POST)
	public List<Map<String,Object>> searchWarnRecord(String type,String no){
		return warnRecordService.searchWarnRecord(type,no);
	}	
	
}
