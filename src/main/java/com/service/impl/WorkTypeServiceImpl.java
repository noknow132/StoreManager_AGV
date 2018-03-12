package com.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.IWorkTypeDao;
import com.entity.WorkType;
import com.service.IWorkTypeService;

@Service
public class WorkTypeServiceImpl implements IWorkTypeService{
	@Autowired
	private IWorkTypeDao workTypeDao;

	//查询所有作业类型
	@Override
	public List<WorkType> searchWorkType() {
		return workTypeDao.searchWorkType();
	}

	//新增或修改作业类型
	@Override
	public Map<String, Object> saveWorkType(WorkType workType) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			if(workType.getTypeId()==null || workType.getTypeId().equals("")){
				//新增
				WorkType wt = new WorkType();
				wt.setTypeId(UUID.randomUUID().toString().replace("-", ""));
				wt.setTypeName(workType.getTypeName());
				wt.setTypeStatue(1);
				//wt.setCreateTime(new Date());
				workTypeDao.insertWorkType(wt);
			}else{
				//修改
				WorkType wt = workTypeDao.searchWorkTypeById(workType.getTypeId());
				wt.setTypeName(workType.getTypeName());
				workTypeDao.updateWorkType(wt);
			}
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
		}
		return result;
	}

	//启用或禁用作业类型
	@Override
	public Map<String, Object> updateWorkTypeById(String workTypeId, String isUsed) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			WorkType workType = workTypeDao.searchWorkTypeById(workTypeId);
			workType.setTypeStatue(Integer.parseInt(isUsed));
			workTypeDao.updateWorkType(workType);
			result.put("stus", 200);
		} catch (Exception e) {
			result.put("stus", 500);
			e.printStackTrace();
		}
		return result;
	}

	//根据id查找作业类型
	@Override
	public WorkType searchWorkTypeById(String workTypeId) {
		return workTypeDao.searchWorkTypeById(workTypeId);
	}

}
