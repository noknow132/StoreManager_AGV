package com.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entity.WorkStep;
import com.service.IWorkStepService;
/**
 * @author 罗欢欢
 * @date 2018-1-15
 * @remark 当前作业业务
 */

@RestController
@RequestMapping("/workStep")
public class WorkStepController {
	@Autowired
	private IWorkStepService workStepService;
	

	//删除未完成的作业
	@RequestMapping(value="/delWorkStepsUnfinished",method = RequestMethod.POST)
	public Map<String, Object>  delWorkStepsUnfinished(@RequestBody String[] workIds){
		    return workStepService.delWorkStepsUnfinished(workIds);
	}
	//删除已完成的任务
	@RequestMapping(value="/delWorkStepsFinished",method = RequestMethod.POST)
	public Map<String, Object>  delWorkStepsFinished(@RequestBody String[] workIds){
		    return workStepService.delWorkStepsFinished(workIds);
	}
	//根据状态查询作业
	@RequestMapping(value="/searchWorkStepsByWorkStatue",method = RequestMethod.POST)
	public Map<String, Object>  searchWorkStepsByWorkStatue(Integer workStatue,Integer workType,String no ){
		    if(workType==null){
		    	workType=-1;
		    }
		    return workStepService.searchWorkStepsByWorkStatue(workStatue,workType,no);
	}
	
	//完成当前执行中的作业
	@RequestMapping(value = "/finishCurrentWorkStep")
	public void finishCurrentWorkStep() {
		workStepService.finishCurrentWorkStep();
	}
	
	//根据执行状态查找作业
	@RequestMapping(value = "/searchWorkStepByStatue")
	public List<WorkStep> searchWorkStepByStatue(String status) {
		return workStepService.searchWorkStepByStatue(status);
	}

	//获取机械臂当前位置
	@RequestMapping(value = "/getNowPlace")
	public Map<String,Object> getNowPlace(){	
		return workStepService.getNowPlace();
	}	
	
	//启动机器，执行任务(PLC)
	@RequestMapping(value = "/startWorkStep")
	public Map<String,Object> startWorkStep(String code){	
		return workStepService.startWorkStep(code);
	}
	//改变扫描的入库作业变为待执行
	@RequestMapping(value = "/changeWorkStatus")
	public Map<String,Object> changeWorkStatus(String fringeCode,int workStatue){	
		return workStepService.changeWorkStatus(fringeCode,workStatue);
	}
}
