package com.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dao.ICreateStoreAreaDao;
import com.entity.CreateStoreArea;
import com.entity.CreateStoreHouse;
import com.entity.UserInfo;
import com.service.ICreateStoreAreaService;

@RestController
@RequestMapping("/CreateStoreAreaController")
public class CreateStoreAreaController {
	@Autowired
	private ICreateStoreAreaService createStoreAreaService;
	
	//新增区位
	@RequestMapping(value="/addCreateStoreArea",method = RequestMethod.POST)
	public Map<String, Object>  addCreateStoreArea(CreateStoreArea createStoreArea,HttpServletRequest request){
		 Map<String,Object> loginUser=(Map<String, Object>) request.getSession().getAttribute("LOGINEDUSER");
		 UserInfo ui=(UserInfo) loginUser.get("userBase");
		 String userId = ui.getUserId();
		    return createStoreAreaService.addCreateStoreArea(createStoreArea,userId);
	}
	
	//根据建库 查找区位
	@RequestMapping(value="/searchCreateStoreAreaByCreateStoreHouseId",method = RequestMethod.POST)
	public List<Map<String,Object>> searchCreateStoreAreaByCreateStoreHouseId(String CreateStoreHouseId){
		return createStoreAreaService.searchCreateStoreAreaByCreateStoreHouseId(CreateStoreHouseId);
	}
	
	//检查区位名称是否存在
	@RequestMapping("/checkAreaNameExist")
	public Map<String,Boolean> checkAreaNameExist(String areaName,String createstorehouseId){
		return createStoreAreaService.checkAreaNameExist(areaName,createstorehouseId);	
	}
	
	//根据区位id 查询区位信息
	@RequestMapping(value = "/selectCreateStoreAreaById", method = RequestMethod.POST)
	public CreateStoreArea selectCreateStoreAreaById(String createStoreAreaId) {
		return createStoreAreaService.selectCreateStoreAreaById(createStoreAreaId);
	}
}
