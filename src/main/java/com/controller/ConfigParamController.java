package com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.entity.ConfigParam;
import com.entity.WarnRecord;
import com.service.IConfigParamService;
import com.util.plcconn.PLCController;

@Controller
@RequestMapping("/ConfigParamController")
public class ConfigParamController {
	
	@Autowired
	private IConfigParamService configParamService;
	
	//一键初始化
	@RequestMapping(value = "/initByOnekey"  ,method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> initByOnekey(){
		return configParamService.initByOnekey();
	}
	
	//查询参数配置
	@RequestMapping(value = "/selectConfigParamOne")
	@ResponseBody
	public ConfigParam  selectConfigParamOne(){
		return configParamService.selectConfigParamOne();
	}
	
	//修改系统名称
	@RequestMapping(value = "/updateConfigParamName")
	@ResponseBody
	public Map<String,Object> updateConfigParamName(String name){
		
		return configParamService.updateConfigParamName(name);
	}
	
	//修改plc启动暂停的控制开关
	@RequestMapping(value = "/updateConfigParamIsRun",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateConfigParamIsRun(int isRun){
		return configParamService.updateConfigParamIsRun(isRun);
	}
	//报警系统
	@RequestMapping(value = "/warn")
	@ResponseBody
	public Map<String,Object> warn(){	
		return configParamService.warn();
	}
	
	//确认报警
	@RequestMapping(value = "/solveWarn",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> solveWarn(String warnCode,String warnRecordId) {
		return configParamService.solveWarn(warnCode,warnRecordId);
	}
    
   @RequestMapping("/warn2")
    public String helloHtml(Map<String,Object> map){
        String warn = configParamService.warn().get("warnCode").toString();
        map.put("warn",warn);
        return "currentTask/currentTask";
    }
    
    
	//入口复位
	@RequestMapping(value = "/reset")
	@ResponseBody
	public Map<String,Object> reset(){
		return configParamService.reset();
	}
	
	//入口复位(PLC)
	@RequestMapping(value = "/resetPlace",method = RequestMethod.GET)
	@ResponseBody
	public boolean resetPlace() {
		return PLCController.resetPlace();
	}
	
	
	//判断网络连接
	@RequestMapping(value = "/isNetStateConnect",method = RequestMethod.GET)
	@ResponseBody
	public boolean isNetStateConnect() {
		return PLCController.isNetStateConnect();
	}
	
	//获取本地Ip
	@RequestMapping(value = "/getLocalIp",method = RequestMethod.GET)
	@ResponseBody
	public List<String> getLocalIp() {
		return PLCController.getLocalIp();
	}
	
	//获取申请码
	@RequestMapping(value = "/createRegFile",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createRegFile(String regSchool,String principal,String email,String tel) {
		List<String> info = new ArrayList<String>();
		info.add(regSchool);
		info.add(principal);
		info.add(email);
		info.add(tel);
		return configParamService.createRegFile(info);
	}
	
	//自动判断注册码
	@RequestMapping(value = "/autoRegEdit",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> autoRegEdit() {
		return configParamService.autoRegEdit();
	}

	
	
}
