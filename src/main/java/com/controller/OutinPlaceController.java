package com.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.entity.OutLet;
import com.service.IOutLetService;
import com.service.IOutinPlaceService;
/**
 * @author 罗欢欢
 * @date 2018-03-17
 * @remark 出入口配置
 */

@RestController
@RequestMapping("/outinPlace")
public class OutinPlaceController {
	@Autowired
	private IOutinPlaceService outinPlaceService;
	
	//查询出口
	@RequestMapping(value="/searchOutinPlaceByType",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  searchOutinPlaceByType(Integer type){
		    return outinPlaceService.searchOutinPlaceByType(type);
	}
	
	
}
