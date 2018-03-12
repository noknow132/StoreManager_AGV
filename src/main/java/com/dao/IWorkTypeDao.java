package com.dao;

import java.util.List;

import com.entity.WorkType;

public interface IWorkTypeDao {
	/**
	 * 查询所有作业类型
	 * @return
	 */
	List<WorkType> searchWorkType();
	
	/**
	 * 根据id查询作业类型
	 * @param workTypeId 作业类型id
	 * @return
	 */
	WorkType searchWorkTypeById(String workTypeId);
	
	/**
	 * 修改作业类型
	 * @param workType 作业类型实体
	 * @return
	 */
	int updateWorkType(WorkType workType);
	
	/**
	 * 新增作业类型
	 * @param workType 作业类型实体
	 * @return
	 */
	int insertWorkType(WorkType workType);
}