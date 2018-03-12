package com.service;

import java.util.List;
import java.util.Map;

import com.entity.WorkType;

public interface IWorkTypeService {
	/**
	 * 查询所有作业类型
	 * @return
	 */
	List<WorkType> searchWorkType();
	
	/**
	 * 新增或修改作业类型
	 * @param workType 作业类型实体
	 * @return
	 */
	Map<String,Object> saveWorkType(WorkType workType);
	
	/**
	 * 启用或禁用作业类型
	 * @param workTypeId 作业类型id
	 * @param isUsed 是否禁用
	 * @return
	 */
	Map<String,Object> updateWorkTypeById(String workTypeId,String isUsed);
	
	/**
	 * 根据id查找作业类型
	 * @param workTypeId 作业类型id
	 * @return
	 */
	WorkType searchWorkTypeById(String workTypeId);
}
