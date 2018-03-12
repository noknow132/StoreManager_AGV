package com.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.entity.CreateStoreArea;
import com.entity.CreateStoreHouse;
import com.entity.UserInfo;
import com.service.ICreateStoreHouseService;
/**
 * @author 罗欢欢
 * @date 2018-1-5
 * @remark 建库业务
 */

@RestController
@RequestMapping("/createStoreHouse")
public class CreateStoreHouseController {
	@Autowired
	ICreateStoreHouseService createStoreHouseService;
	
	//新建仓库
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/addCreateStoreHouse",method = RequestMethod.POST)
	public Map<String, Object>  addCreateStoreHouse(CreateStoreHouse cStoreHouse,HttpServletRequest request){
		 Map<String,Object> loginUser=(Map<String, Object>) request.getSession().getAttribute("LOGINEDUSER");
		 UserInfo ui=(UserInfo) loginUser.get("userBase");
		 String userId = ui.getUserId();
		// String userId="123";
		    return createStoreHouseService.addCreateStoreHouse(cStoreHouse,userId);
	}
	
	//新增仓库
	@RequestMapping(value="/insertCreateStoreHouse",method = RequestMethod.POST)
	public Map<String, Object>  insertCreateStoreHouse(CreateStoreHouse cStoreHouse,HttpServletRequest request){
		 Map<String,Object> loginUser=(Map<String, Object>) request.getSession().getAttribute("LOGINEDUSER");
		 UserInfo ui=(UserInfo) loginUser.get("userBase");
		 String userId = ui.getUserId();
		    return createStoreHouseService.insertCreateStoreHouse(cStoreHouse,userId);
	}
	
	//编辑区位
	@RequestMapping(value="/editCreateStoreHouse2",method = RequestMethod.POST)
	public Map<String, Object>  editCreateStoreHouse2(CreateStoreArea createStoreArea){
		    return createStoreHouseService.editCreateStoreHouse2(createStoreArea);
	}
	
	//编辑仓库
	@RequestMapping(value="/editCreateStoreHouse",method = RequestMethod.POST)
	public Map<String, Object>  editCreateStoreHouse(CreateStoreHouse cStoreHouse){
		    return createStoreHouseService.editCreateStoreHouse(cStoreHouse);
	}
	//查询建库信息
	@RequestMapping(value="/searchCreateStoreHouse",method = RequestMethod.POST)
	public Map<String, Object>  searchCreateStoreHouse(){
		    return createStoreHouseService.searchCreateStoreHouse();
	}
	//查询建库信息
	@RequestMapping(value="/searchCreateStoreHouse2",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  searchCreateStoreHouse2(){
		    return createStoreHouseService.searchCreateStoreHouse();
	}
	//查询建库信息根据区位名
	@RequestMapping(value="/searchCreateStoreHouseByAreaName",method = RequestMethod.POST)
	public Map<String, Object>  searchCreateStoreHouseByAreaName(String areaName){
		    return createStoreHouseService.searchCreateStoreHouseByAreaName(areaName);
	}
	//查询所有的区位名字
	@RequestMapping(value="/searchAreaNames",method = RequestMethod.POST)
	public Map<String, Object>  searchAreaNames(){
	    return createStoreHouseService.searchAreaNames();
	}
	
	
	//检查区位名称是否存在
	@RequestMapping("/checkAreaNameExist")
	public Map<String,Boolean> checkAreaNameExist(String areaName,String createstorehouseId){
		return createStoreHouseService.checkAreaNameExist(areaName,createstorehouseId);	
	}
	
	/**********凡哥************/
	//根据名称分组查询仓库
	@RequestMapping(value="/searchCreateStoreHouseGroupByStoreName",method = RequestMethod.GET)
	public List<Map<String,Object>> searchCreateStoreHouseGroupByStoreName(){
		return createStoreHouseService.searchCreateStoreHouseGroupByStoreName();
	}
	
	//根据建库名称查找建库
	@RequestMapping(value="/searchCreateStoreHouseByStoreName",method = RequestMethod.POST)
	public List<CreateStoreHouse> searchCreateStoreHouseByStoreName(String storeName){
		return createStoreHouseService.searchCreateStoreHouseByStoreName(storeName);
	}
	
	//仓库基本信息
	@RequestMapping(value = "/searchStoreBaseInfo", method = RequestMethod.POST)
	public Map<String, Object> searchStoreBaseInfo(String createStoreHouseId) {
		return createStoreHouseService.searchStoreBaseInfo(createStoreHouseId);
	}
	
	//根据建库id查找建库
	@RequestMapping(value = "/selectCreateStoreHouseById", method = RequestMethod.POST)
	public CreateStoreHouse selectCreateStoreHouseById(String createStoreId) {
		return createStoreHouseService.selectCreateStoreHouseById(createStoreId);
	}
	
	//删除区位
	@RequestMapping(value = "/deleteCreateStoreAreaById", method = RequestMethod.POST)
	public Map<String,Object> deleteCreateStoreAreaById(String createstoreareaId) {
		return createStoreHouseService.deleteCreateStoreAreaById(createstoreareaId);
	}
	
	//删除仓库
	@RequestMapping(value = "/deleteCreateStoreHouseById", method = RequestMethod.POST)
	public Map<String,Object> deleteCreateStoreHouseById(String createstorehouseId) {
		return createStoreHouseService.deleteCreateStoreHouseById(createstorehouseId);
	}
	
	//查询已建仓库数目
	@RequestMapping(value = "/searchCreateStoreHouseAllCount", method = RequestMethod.GET)
	public int searchCreateStoreHouseAllCount() {
		return createStoreHouseService.searchCreateStoreHouseAllCount();
	}
	
	
}
