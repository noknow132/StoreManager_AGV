package com.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.entity.CreateStoreHouse;
import com.entity.OutputStoreDto;
import com.entity.Temp;
import com.entity.WorkStep;
/**
 * @author 罗欢欢
 * @date 2018-1-15
 * @remark 当前作业的业务逻辑接口
 */
public interface IWorkStepService {

	/**
	 * 删除未完成的作业
	 * @param workIds 作业id数组
	 * @return
	 */
	Map<String, Object>  delWorkStepsUnfinished( String[] workIds);

	/**
	 * 删除已完成的作业
	 * @param workIds 作业id数组
	 * @return
	 */
	Map<String, Object> delWorkStepsFinished( String[] workIds);

	/**
	 * 根据状态查找作业
	 * @param workStatue 状态
	 * @param workType 作业类型
	 * @param no 搜索条件编号
	 * @return 执行结果及查找列表
	 */
	Map<String, Object> searchWorkStepsByWorkStatue(Integer workStatue,Integer workType,String no);

	/**
	 * 完成当前执行中的作业
	 */
	void finishCurrentWorkStep();

	/**
	 * 取消当前执行中的作业
	 */
	void cancleCurrentWorkStep();
	
	/**
	 * 根据执行状态查找作业
	 * @param status 作业状态
	 * @return
	 */
	List<WorkStep> searchWorkStepByStatue(String status);


	/**
	 * 获取机械臂的当前位置
	 * @return
	 */
	Map<String,Object> getNowPlace();
	
	/**
	 * 启动机器，执行任务(PLC)
	 * @return
	 */
	Map<String,Object> startWorkStep(String code);
	
	/**
	 * 改变扫描的入库作业变为待执行
	 * @param fringeCode
     * @param workStatue 状态
	 * @return
	 */
	Map<String,Object> changeWorkStatus(String fringeCode,int workStatue);
}