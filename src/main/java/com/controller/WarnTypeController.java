package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entity.WarnType;
import com.service.IWarnTypeService;

@RestController
@RequestMapping("/WarnTypeController")
public class WarnTypeController {
	@Autowired
	private IWarnTypeService warnTypeService;
	
	//查询报警类型
	@RequestMapping(value="searchWarnTypeAll",method = RequestMethod.GET)
	private List<WarnType> searchWarnTypeAll(){
		return warnTypeService.searchWarnTypeAll();
	}
}
