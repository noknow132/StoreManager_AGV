package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.entity.PlcConnConfig;
import com.service.IPlcConnConfigService;

@RestController
@RequestMapping("/PlcConnConfigController")
public class PlcConnConfigController {
	@Autowired
	private IPlcConnConfigService plcConnConfigService;
	
	//手动测试连接(新增或修改IP地址到数据库)
	@RequestMapping(value = "/saveIp", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveIp(String localIp, String PLCIP, int PLCPort,String PLCId) {
		return plcConnConfigService.saveIp(localIp, PLCIP, PLCPort,PLCId);
	}
	
	//自动测试连接
	@RequestMapping(value = "/testConnect", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> testConnect() {
		return plcConnConfigService.testConnect();
	}
	
	//连接PLC
	@RequestMapping(value = "/plcConn", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> plcConn() {
		return plcConnConfigService.connectPLC();
	}
	
	//通过plc名称连接plc
	@RequestMapping(value = "/plcConnById", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> plcConnById(String plcName) {
		return plcConnConfigService.plcConnById(plcName);
	}
	
	//通过plc名称查找plc配置
	@RequestMapping(value = "/searchPlcConfigByName", method = RequestMethod.POST)
	@ResponseBody
	public PlcConnConfig searchPlcConfigByName(String plcName) {
		return plcConnConfigService.searchPlcConfigByName(plcName);
	}
	
	//通过plc类型查找plc配置
	@RequestMapping(value = "/searchPlcConnConfigByType", method = RequestMethod.POST)
	@ResponseBody
	public List<PlcConnConfig> searchPlcConnConfigByType(String plcType) {
		return plcConnConfigService.searchPlcConnConfigByType(plcType);
	}
	
	//新增plc连接配置
	@RequestMapping(value = "/insertPlcConfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> insertPlcConfig(PlcConnConfig plcConnConfig) {
		return plcConnConfigService.insertPlcConfig(plcConnConfig);
	}
	
	//删除plc连接配置
	@RequestMapping(value = "/deletePlcConfigById", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deletePlcConfigById(String id) {
		return plcConnConfigService.deletePlcConfigById(id);
	}
	
}
