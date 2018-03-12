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
/**
 * @author 罗欢欢
 * @date 2018-1-12
 * @remark 出口业务
 */

@RestController
@RequestMapping("/outLet")
public class OutLetController {
	@Autowired
	private IOutLetService outLetService;
	
	//查询出口
	@RequestMapping(value="/searchOutLetByType",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  searchOutLetByType(Integer type){
		    return outLetService.searchOutLetByType(type);
	}
	
	//查询所有出口
	@RequestMapping("/searchOutLet")
	public List<Map<String,Object>> searchOutLet(String outType) {
		return outLetService.searchOutLet(outType);
	}
	
	//查询所有出口11111111111
	@RequestMapping("/searchOutLet1")
	public Map<String, Object> searchOutLet1(String outType) {
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = outLetService.searchOutLet(outType);
		map.put("rows", list);
		map.put("total", list.size());
		
		return map;
	}

	// 新建出口
	@RequestMapping("/saveOutLet")
	public Map<String, Object> saveOutLet(String outletName,Integer outNo,Integer outType,String outletId) {
		return outLetService.saveOutLet(outletName, outNo, outType,outletId);
	}
	
	//启用或禁用出口
	@RequestMapping("/updateOutLetIsUsed")
	public Map<String, Object> updateOutLetIsUsed(String isUsed,String outletId) {
		return outLetService.updateOutLetIsUsed(isUsed,outletId);
	}
	
	//根据id查找出口
	@RequestMapping("/searchOutLetById")
	public OutLet searchOutLetById(String outletId) {
		return outLetService.searchOutLetById(outletId);
	}
}
